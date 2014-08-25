import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Universe class is the main worker of the program. It defines all the
 * physics parameters, has the thread that runs the physics, and controls all
 * the buttons and what they do.
 * 
 * @author Christopher Glasz
 */
public class Universe implements ThreadListener {

	/**
	 * The number of times the program completes one iteration of the physics
	 * calculations per second.
	 */
	public static double calcsPS;

	/**
	 * The Applet itself
	 */
	private Applet myApplet;

	/**
	 * A thread to control the physics
	 */
	private Threader physicsThread;

	/**
	 * A particle system to hold all our particles
	 */
	private ParticleSystem myParticleSystem;

	/**
	 * Buttons which will control the Applet
	 */
	private Button clear, run, thetaUp, thetaDown, countUp, countUp2,
			countDown, countDown2, massUp, massDown, massUp2, massDown2,
			softenerUp, softenerDown, radiusUp, radiusDown, revealTree, 
			viewMode, pause, follow, populateRule, simMode;

	/**
	 * Labels to convey information about the current state of physics
	 */
	private Label thetaLabel, particleCountLabel, particleMassLabel,
			softenerLabel, radiusLabel;

	/**
	 * Constant to hold the window width
	 */
	public static final int WINDOW_WIDTH = Simulation.WINDOW_WIDTH;

	/**
	 * Constant to hold the window height
	 */
	public static final int WINDOW_HEIGHT = Simulation.WINDOW_HEIGHT;

	/**
	 * The universal gravitational constant
	 */
	public static final double G = 6.67384e-11;

	/**
	 * The number of particles to add to the system
	 */
	private int particleCount;

	/**
	 * The number of times a full iteration of the physics calculations has been
	 * completed since the last time the user hit the run button
	 */
	public static int timesteps;

	/**
	 * Epsilon is the softening parameter. Without it, the particles behave
	 * erratically, a poor simulation of physics. Low values result in nearby
	 * particles being more strongly attracted to one another. High values
	 * result in a smoother simulation
	 */
	public static double epsilon;

	/**
	 * Theta defines what constitutes the distance at which particles are
	 * considered "sufficiently far away" for force acting on it to be
	 * summarized using the quadtree. Higher thetas produce results faster, but
	 * less accurate. Very low thetas make for more accurate simulations, but
	 * are far slower. For example, with a theta of 1, a two dimensional
	 * simulation of 10,000 particles runs at about 15 (14.9992) physics
	 * calculations per second. Keeping theta at 0.5, the default, will result
	 * in a speed of about 7 (7.0418) calculations per second. At 0, the
	 * simulation is Big O of n^2, and runs at just 0.1 (0.1005) calculations
	 * per second
	 */
	public static double theta;

	/**
	 * Boolean to determine whether to display the quadtree
	 */
	public static boolean showTree;

	/**
	 * Boolean to determine whether to color each particle by its net force
	 */
	public static boolean colorByForce;

	/**
	 * Boolean to determine whether to follow the center of mass
	 */
	public static boolean followCenter;

	/**
	 * Boolean to determine whether to simulate the system in three dimensions
	 */
	public static boolean simulate3D;

	/**
	 * Boolean to keep track of whether there are particles in the system
	 */
	private boolean populated;

	/**
	 * Boolean to control whether physics is in play
	 */
	private boolean paused;

	/**
	 * Booleans to determine how particles will be added
	 */
	private boolean dynamicCircle;

	/**
	 * The constructor must receive an Applet (without it we can't do much). It
	 * initializes the threads, sets start values, and creates the particle
	 * system.
	 * 
	 * @param myApplet
	 */
	public Universe(Applet myApplet) {
		this.myApplet = myApplet;

		// Our initial conditions : 
		// A reasonable number of particles
		particleCount = 1000;
		
		// A reasonable mass for them
		Particle.mass = 1e11;
		
		// Very small
		Particle.radius = 1;
		
		// A good starting value for our softening parameter
		epsilon = 2e4;
		
		// A theta that balances between fast and accurate
		theta = 0.5;
		
		// We don't want to show the tree yet
		showTree = false;
		
		// Nor the colors
		colorByForce = false;
		
		// We don't have any particles yet
		populated = false;
		
		// We don't want to start paused
		paused = false;
		
		// We aren't following the center of mass yet
		followCenter = false;
		
		// We'll start in 2 dimensions, keep it simple
		simulate3D = false;

		// We'll start with a plain old field of particles
		dynamicCircle = false;

		// Add all our buttons
		addButtons();

		// Create our particle system
		if (simulate3D)
			myParticleSystem = new ParticleSystem3D();
		else
			myParticleSystem = new ParticleSystem();
		
		// And start our engines!
		physicsThread = new Threader(this);
		physicsThread.start();

	}

	/**
	 * Paints all the particles to the Applet window
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		if (populated)
			myParticleSystem.paint(pane);
	}

	/**
	 * Simply adds all the buttons to the Applet and determines their functions.
	 */
	private void addButtons() {
		clear = new Button("Clear");
		run = new Button("Run");
		pause = new Button("Pause");
		follow = new Button("Follow Center");
		thetaUp = new Button("+");
		thetaDown = new Button("-");
		softenerUp = new Button("x10");
		softenerDown = new Button("x0.1");
		countUp = new Button("x2");
		countUp2 = new Button("x10");
		countDown = new Button("x0.5");
		countDown2 = new Button("x0.1");
		massUp = new Button("x2");
		massDown = new Button("x0.5");
		massUp2 = new Button("x10");
		massDown2 = new Button("x0.1");
		radiusUp = new Button("+");
		radiusDown = new Button("-");
		revealTree = new Button("Show Tree");
		viewMode = new Button("Color by Force");
		simMode = new Button("2D");
		populateRule = new Button("Static Field");

		clear.setFont(Simulation.myFont);
		run.setFont(Simulation.myFont);
		pause.setFont(Simulation.myFont);
		follow.setFont(Simulation.myFont);
		thetaUp.setFont(Simulation.myFont);
		thetaDown.setFont(Simulation.myFont);
		softenerUp.setFont(Simulation.myFont);
		softenerDown.setFont(Simulation.myFont);
		countUp.setFont(Simulation.myFont);
		countUp2.setFont(Simulation.myFont);
		countDown.setFont(Simulation.myFont);
		countDown2.setFont(Simulation.myFont);
		massUp.setFont(Simulation.myFont);
		massUp2.setFont(Simulation.myFont);
		massDown.setFont(Simulation.myFont);
		massDown2.setFont(Simulation.myFont);
		radiusUp.setFont(Simulation.myFont);
		radiusDown.setFont(Simulation.myFont);
		revealTree.setFont(Simulation.myFont);
		viewMode.setFont(Simulation.myFont);
		simMode.setFont(Simulation.myFont);
		populateRule.setFont(Simulation.myFont);
		
		thetaLabel = new Label("Theta\n" + theta);
		particleCountLabel = new Label("Particles to Add\n" + particleCount);
		particleMassLabel = new Label(String.format("Particle Mass\n %.1e",
				Particle.mass));
		softenerLabel = new Label(String.format("Epsilon\n %.0e", epsilon));
		radiusLabel = new Label(String.format("Particle Radius\n %d", Particle.radius));

		// Pause pauses the physics
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paused = !paused;
				if (paused) {
					pause.setLabel("Play");
				} else {
					pause.setLabel("Pause");
				}
			}
		});
		
		// Follow follows the center of mass
		follow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				followCenter = !followCenter;
				if (followCenter) {
					follow.setLabel("Stop Following");
				} else {
					follow.setLabel("Follow Center");
				}
			}
		});
		
		// SimMode determines which dimension we're operating in
		simMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulate3D = !simulate3D;
				if (simulate3D) {
					simMode.setLabel("3D");
					myParticleSystem = new ParticleSystem3D();
				} else {
					simMode.setLabel("2D");
					myParticleSystem = new ParticleSystem();
				}
			}
		});
		
		// Clear dumps all the particles
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (simulate3D) 
					myParticleSystem = new ParticleSystem3D();
				else
					myParticleSystem = new ParticleSystem();
				simMode.setEnabled(true);
				populated = false;
				timesteps = 0;
			}
		});
		
		// PopulateRule switches between our two population methods
		populateRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dynamicCircle = !dynamicCircle;
				if (!dynamicCircle) {
					populateRule.setLabel(" Static Field ");
				} else {
					populateRule.setLabel("Galaxy");
				}
			}
		});
		
		// Run populates our system
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Pause while we add stuff
				boolean pauseHolder = paused;
				paused = true;
				
				// We can't change dimensions while particles exist
				simMode.setEnabled(false);
				
				if (dynamicCircle) {
					// Center
					double x = WINDOW_WIDTH / 2;
					double y = WINDOW_HEIGHT / 2;
					double z = WINDOW_HEIGHT / 2;

					// Mass
					double m = Particle.mass * particleCount;
					// Radius
					double r = WINDOW_HEIGHT / 2;
					// Density
					double p = m / (Math.PI * r * r);

					for (int i = 0; i < particleCount; i++) {

						// Evenly distributes particles through a disk
						double u = Math.random();
						double v = Math.random();
						double w = r * Math.sqrt(u);
						double t = 2 * Math.PI * v;
						x += w * Math.cos(t);
						y += w * Math.sin(t);
						z += -25 + 50 * Math.random();

						Particle particle;

						if (simulate3D) {
							particle = new Particle3D(x, y, z);
							x = WINDOW_WIDTH / 2;
							y = WINDOW_HEIGHT / 2;
							z = WINDOW_HEIGHT / 2;
							particle.giveCircularOrbit(x, y, r, p);
							((ParticleSystem3D) myParticleSystem).add(particle);
						} else {
							particle = new Particle(x, y);
							x = WINDOW_WIDTH / 2;
							y = WINDOW_HEIGHT / 2;
							particle.giveCircularOrbit(x, y, r, p);
							myParticleSystem.add(particle);
						}

					}
				} else {
					for (int i = 0; i < particleCount; i++) {
						Particle particle;
						if (simulate3D) {
							particle = new Particle3D(WINDOW_WIDTH
									* Math.random(), WINDOW_HEIGHT
									* Math.random(), WINDOW_HEIGHT
									* Math.random());
							((ParticleSystem3D) myParticleSystem).add(particle);
						} else {
							particle = new Particle(WINDOW_WIDTH
									* Math.random(), WINDOW_HEIGHT
									* Math.random());
							myParticleSystem.add(particle);
						}
					}
				}
				// We have particles
				populated = true;
				
				// Reset the time
				timesteps = 0;
				
				// Press play
				paused = pauseHolder;
			}
		});
		
		// Reveal tree reveals the tree
		revealTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTree = !showTree;
				if (showTree) {
					revealTree.setLabel("Hide Tree");
				} else {
					revealTree.setLabel("Show Tree");
				}
				myApplet.repaint();
			}
		});

		// ViewMode switches between black and white and colored particles
		viewMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorByForce = !colorByForce;
				myApplet.repaint();
			}
		});
		
		// Count up doubles the number of particle to add
		countUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				particleCount *= 2;
				if (particleCount > 200000)
					particleCount = 200000;
				particleCountLabel
						.setText("Particles to Add\n" + particleCount);
			}
		});
		
		// Count down divides it in two
		countDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				particleCount /= 2;
				if (particleCount < 1)
					particleCount = 1;
				particleCountLabel
						.setText("Particles to Add\n" + particleCount);
			}
		});
		
		// Count up II increases it tenfold
		countUp2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				particleCount *= 10;
				if (particleCount > 200000)
					particleCount = 200000;
				particleCountLabel
						.setText("Particles to Add\n" + particleCount);
			}
		});
		
		// Count down II divides my ten
		countDown2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				particleCount /= 10;
				if (particleCount < 1)
					particleCount = 1;
				particleCountLabel
						.setText("Particles to Add\n" + particleCount);
			}
		});
		
		// Radius up increases the radius (really it's the diameter) by 1
		radiusUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.radius += 1;
				radiusLabel.setText(String.format("Particle Radius\n%d",
						Particle.radius));
			}
		});
		
		// Radius down decrements it
		radiusDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.radius -= 2;
				if (Particle.radius < 1)
					Particle.radius = 1;
				radiusLabel.setText(String.format("Particle Radius\n%d",
						Particle.radius));
			}
		});
		
		// Mass up doubles the mass
		massUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.mass *= 2;
				particleMassLabel.setText(String.format("Particle Mass\n%.1e",
						Particle.mass));
			}
		});
		
		// Mass down halves it
		massDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.mass /= 2;
				if (Particle.mass < 1)
					Particle.mass = 1;
				particleMassLabel.setText(String.format("Particle Mass\n%.1e",
						Particle.mass));
			}
		});
		
		// Mass up II increases it tenfold
		massUp2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.mass *= 10;
				particleMassLabel.setText(String.format("Particle Mass\n%.1e",
						Particle.mass));
			}
		});
		
		// Mass down II divides it by ten
		massDown2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Particle.mass /= 10;
				if (Particle.mass < 1)
					Particle.mass = 1;
				particleMassLabel.setText(String.format("Particle Mass\n%.1e",
						Particle.mass));
			}
		});
		
		// Theta up increases theta by 0.1
		thetaUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				theta += 0.1;
				thetaLabel.setText(String.format("Theta\n%.1f", theta));
			}
		});
		
		// Theta down decreases it by 0.1
		thetaDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				theta -= 0.1;
				thetaLabel.setText(String.format("Theta\n%.1f", theta));
			}
		});
		
		// Softener up increases epsilon tenfold
		softenerUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				epsilon *= 10;
				softenerLabel.setText(String.format("Epsilon\n%.0e", epsilon));
			}
		});
		
		// Softener down divides it by ten
		softenerDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				epsilon /= 10;
				softenerLabel.setText(String.format("Epsilon\n%.0e", epsilon));
			}
		});
		
		// Aesthetic stuff
		thetaLabel.setForeground(Color.WHITE);
		thetaLabel.setAlignment(Label.CENTER);
		thetaLabel.setFont(Simulation.myFont);
		particleCountLabel.setForeground(Color.WHITE);
		particleCountLabel.setAlignment(Label.CENTER);
		particleCountLabel.setFont(Simulation.myFont);
		particleMassLabel.setForeground(Color.WHITE);
		particleMassLabel.setAlignment(Label.CENTER);
		particleMassLabel.setFont(Simulation.myFont);
		radiusLabel.setForeground(Color.WHITE);
		radiusLabel.setAlignment(Label.CENTER);
		radiusLabel.setFont(Simulation.myFont);
		softenerLabel.setForeground(Color.WHITE);
		softenerLabel.setAlignment(Label.CENTER);
		softenerLabel.setFont(Simulation.myFont);
		
		// Add all the buttons and labels in the right order
		myApplet.add(clear);
		myApplet.add(run);
		myApplet.add(countDown2);
		myApplet.add(countDown);
		myApplet.add(particleCountLabel);
		myApplet.add(countUp);
		myApplet.add(countUp2);
		myApplet.add(massDown2);
		myApplet.add(massDown);
		myApplet.add(particleMassLabel);
		myApplet.add(massUp);
		myApplet.add(massUp2);
		myApplet.add(radiusDown);
		myApplet.add(radiusLabel);
		myApplet.add(radiusUp);
		myApplet.add(thetaDown);
		myApplet.add(thetaLabel);
		myApplet.add(thetaUp);
		myApplet.add(softenerDown);
		myApplet.add(softenerLabel);
		myApplet.add(softenerUp);
		myApplet.add(simMode);
		myApplet.add(populateRule);
		myApplet.add(revealTree);
		myApplet.add(viewMode);
		myApplet.add(follow);
		myApplet.add(pause);
		pause.setLocation(100, 100);
	}

	/**
	 * The method called by the physics thread. If the Applet is not paused and
	 * the particle system has been initialized, it tells the system to simulate
	 * physics at each time step, and then displays the results in the Applet
	 * window.
	 */
	public void timeStep() {
		if (myParticleSystem != null) {
			if (!paused) {
				long a = System.nanoTime();
				myParticleSystem.simulate();
				long b = System.nanoTime();
				long time = (b - a);
				calcsPS = (1e9 / (double) time);
				timesteps++;
			}
			myApplet.repaint();
		}

	}
}
