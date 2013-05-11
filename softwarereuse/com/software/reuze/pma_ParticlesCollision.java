package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac CollisionSystem.java
 *  Execution:    java CollisionSystem               (50 random particles)
 *                java CollisionSystem 1 < input.txt     (from a file) 
 *  
 *  Creates N random particles and simulates their motion according
 *  to the laws of elastic collisions.
 *
 *************************************************************************/

//TODOimport java.awt.Color;


public class pma_ParticlesCollision {
    private d_QueuePriorityGeneric<Event> pq;        // the priority queue
    private double t  = 0.0;        // simulation clock time
    private double Hz = 0.5;        // number of redraw events per clock tick
    private pma_Particle[] particles;   // the array of particles

    // create a new collision system with the given set of particles
    public pma_ParticlesCollision(pma_Particle[] particles) {
        this.particles = particles;
    }

    // updates priority queue with all new events for particle a
    private void predict(pma_Particle a, double limit) {
        if (a == null) return;

        // particle-particle collisions
        for(int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if(t + dt <= limit)
                pq.insert(new Event(t + dt, a, particles[i]));
        }

        // particle-wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
    }

    // redraw all particles
    private void redraw(double limit) {
        /*TODOvgu_StdDraw.clear();
        for(int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        vgu_StdDraw.show(20);
        if (t < limit) {
            pq.insert(new Event(t + 1.0 / Hz, null, null));
        }*/
    }

      
   /********************************************************************************
    *  Event based simulation for limit seconds
    ********************************************************************************/
    public void simulate(double limit) {
        
        // initialize PQ with collision events and redraw event
        pq = new d_QueuePriorityGeneric<Event>();
        for(int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.insert(new Event(0, null, null));        // redraw event


        // the main event-driven simulation loop
        while(!pq.isEmpty()) { 

            // get impending event, discard if invalidated
            Event e = pq.delMin();
            if(!e.isValid()) continue;
            pma_Particle a = e.a;
            pma_Particle b = e.b;

            // physical collision, so update positions, and then simulation clock
            for(int i = 0; i < particles.length; i++)
                particles[i].move(e.time - t);
            t = e.time;

            // process event
            if      (a != null && b != null) a.bounceOff(b);              // particle-particle collision
            else if (a != null && b == null) a.bounceOffVerticalWall();   // particle-wall collision
            else if (a == null && b != null) b.bounceOffHorizontalWall(); // particle-wall collision
            else if (a == null && b == null) redraw(limit);               // redraw event

            // update the priority queue with new collisions involving a or b
            predict(a, limit);
            predict(b, limit);
        }
    }


   /*************************************************************************
    *  An event during a particle collision simulation. Each event contains
    *  the time at which it will occur (assuming no supervening actions)
    *  and the particles a and b involved.
    *
    *    -  a and b both null:      redraw event
    *    -  a null, b not null:     collision with vertical wall
    *    -  a not null, b null:     collision with horizontal wall
    *    -  a and b both not null:  binary collision between a and b
    *
    *************************************************************************/
    private class Event implements Comparable<Event> {
        private final double time;         // time that event is scheduled to occur
        private final pma_Particle a, b;       // particles involved in event, possibly null
        private final int countA, countB;  // collision counts at event creation
                
        
    // create a new event to occur at time t involving a and b
    public Event(double t, pma_Particle a, pma_Particle b) {
        this.time = t;
        this.a    = a;
        this.b    = b;
        if (a != null) countA = a.count();
        else           countA = -1;
        if (b != null) countB = b.count();
        else           countB = -1;
    }

    // compare times when two events will occur
    public int compareTo(Event that) {
        if      (this.time < that.time) return -1;
        else if (this.time > that.time) return +1;
        else                            return  0;
    }
        
    // has any collision occurred between when event was created and now?
    public boolean isValid() {
        if (a != null && a.count() != countA) return false;
        if (b != null && b.count() != countB) return false;
        return true;
    }
   
}



   /********************************************************************************
    *  Sample client
    ********************************************************************************/
    public static void main(String[] args) {

        // remove the border
    	//TODOvgu_StdDraw.setXscale(1.0/22.0, 21.0/22.0);
    	//TODOvgu_StdDraw.setYscale(1.0/22.0, 21.0/22.0);

        // turn on animation mode
    	//TODOvgu_StdDraw.show(0);

        // the array of particles
        pma_Particle[] particles;

        // create N random particles
        if (args.length != 1) {
            int N = 50;
            particles = new pma_Particle[N];
            for(int i = 0; i < N; i++) particles[i] = new pma_Particle();
        }

        // or read from standard input
        else {
            int N = f_StdIn.readInt();
            particles = new pma_Particle[N];
            for(int i = 0; i < N; i++) {
                double rx     = f_StdIn.readDouble();
                double ry     = f_StdIn.readDouble();
                double vx     = f_StdIn.readDouble();
                double vy     = f_StdIn.readDouble();
                double radius = f_StdIn.readDouble();
                double mass   = f_StdIn.readDouble();
                int r         = f_StdIn.readInt();
                int g         = f_StdIn.readInt();
                int b         = f_StdIn.readInt();
                particles[i] = new pma_Particle(rx, ry, vx, vy, radius, mass, (r<<16)+(g<<8)+b);
            }
        }

        // create collision system and simulate
        pma_ParticlesCollision system = new pma_ParticlesCollision(particles);
        system.simulate(10000);
    }
      
}
