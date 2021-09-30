import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.time.LocalTime;
import java.util.Iterator;

public class cv01 {
	int width = 800;
	int height = 600;

	long window;
	GLFWErrorCallback errorCallback;
	GLFWKeyCallback keyCallback;

	void spusti() {
		try {
			init();
			GL.createCapabilities();
			loop();

			glfwDestroyWindow(window);
			keyCallback.free();
		} finally {
			glfwTerminate();
			errorCallback.free();
		}
	}

	void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Chyba pri inicializacii GLFW!!!");

		window = glfwCreateWindow(width, height, "UGR1", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Chyba pri vytvoreni GLFW okna!!!");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, true);
			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);

	}

	void Bressenham(int x1, int y1, int x2, int y2) {
		boolean vymena = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		if(vymena)
		{
			int pomoc = x1;x1=y1; y1 = pomoc;
			pomoc = x2;x2=y2; y2 = pomoc;
		}
		if(x1>x2)
		{
			int pomoc = x2;
			x2 = x1;
			x1 = pomoc;
			pomoc = y2;
			y2 = y1;
			y1 = pomoc;
		}
		int dX = x2 - x1;
		int dY = Math.abs(y2 - y1);
		int y = y1;
		int k1 = 2 * dY;
		int k2 = 2 * dY - 2 * dX;
		int d = 2 * dY - dX;
		int krok = y2 >= y1 ? 1 : -1;
		for (int i = x1; i <= x2; i++) {
			if(vymena) {
				glVertex2i(y, i);
			}
			else
			{
				glVertex2i(i, y);
			}
			if (d < 0) {
				d = d + k1;
			} else {
				d = d + k2;
				y = y + krok;
			}
		}
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	void useckaDDA(int x1, int y1, int x2, int y2) {
		double y = y1;
		double k = (y2 - y1) / (x2 - x1);
		for (int x = x1; x <= x2; x++) {
			y += k;
			glVertex2i(x, (int) y);
		}
		glEnd();

	}
	void midpoint(int center_x,int center_y, int r)
	{
		
		int x = 0;  int y = r;
		 int d = 1-r;
		while (x <= y) {
			glVertex2i(center_x+x, center_y+y);
			glVertex2i(center_x+x, center_y-y);
			glVertex2i(center_x-x, center_y+y);
			glVertex2i(center_x-x, center_y-y);
			glVertex2i(center_x+y,center_y+x);
			glVertex2i(center_x+y, center_y-x);
			glVertex2i(center_x-y, center_y+x);
			glVertex2i(center_x-y, center_y-x);
		 if (d < 0)
		  d= d+(2*x)+3;
		 else {
		  d = d+(2*(x-y))+5;
		  y = y-1;
		 }
		 x = x+1;
		}
	}
	void hodinky()
	{
	    glEnd();
        glPointSize(2);
        glBegin(GL_POINTS);
        glColor3d(1, 1, 1);
		midpoint(width/2, height/2, 290);
		int sx = width/2,sy = height/2; 
		double r2 = Math.min(0.9 * width / 2, 0.9 * height / 2);
		double r1 = Math.min(0.96 * width / 2, 0.96 * height / 2);
		double r3 = Math.min(0.85 * width / 2, 0.85 * height / 2);
        double r4 = Math.min(0.7 * width / 2, 0.7 * height / 2);
        double r5 = Math.min(0.6 * width / 2, 0.6 * height / 2);

        double seconds1 = Math.min(0.8 * width / 2, 0.8 * height / 2);
		for (int uhol = 0; uhol <=360; uhol+=6) {
			if(uhol%30==0)
			{
				Bressenham((int)((sx+r3*Math.cos(Math.toRadians(uhol)))),(int)(sy+r3*Math.sin(Math.toRadians(uhol))),(int)(sx+r1*Math.cos(Math.toRadians(uhol))),(int)(sy+r1*Math.sin(Math.toRadians(uhol))));

			}else
			Bressenham((int)((sx+r2*Math.cos(Math.toRadians(uhol)))),(int)(sy+r2*Math.sin(Math.toRadians(uhol))),(int)(sx+r1*Math.cos(Math.toRadians(uhol))),(int)(sy+r1*Math.sin(Math.toRadians(uhol))));
		}
		//seconds
		glEnd();
		glPointSize(10);
        glBegin(GL_POINTS);
        glColor3d(0, 0, 1);
		int seconds = LocalTime.now().getSecond();
		Bressenham(sx,sy,(int)(sx+seconds1*Math.cos(Math.toRadians(seconds*6-90))),(int)(sy+seconds1*Math.sin(Math.toRadians(seconds*6-90))));
	    //minutes
        glColor3d(0,1,0);
        int minutes= LocalTime.now().getMinute();
        Bressenham(sx,sy,(int)(sx+r4*Math.cos(Math.toRadians(minutes*6+(seconds/10)-90))),(int)(sy+r4*Math.sin(Math.toRadians(minutes*6+(seconds/10)-90))));

        //сas
        glColor3d(1,0,0);
        int hodiny  =  LocalTime.now().getHour();
        int res = 0;
        if(hodiny>=12)
        {
           res=hodiny-12;
        }
        Bressenham(sx,sy,(int)(sx+r5*Math.cos(Math.toRadians(30*res+(minutes/2)-90))),(int)(sy+r5*Math.sin(Math.toRadians(30*res+(minutes/2)-90))));


    }
	/**
	 *
	 */
	/**
	 * 
	 */
	void vykresliGL() {
		/*
		 * int sx = width/2,sy = height/2; double r = 0.8*width/2 >= 0.8*height/2 ?
		 * 0.8*height/2: 0.*width/2; for (int uhol = 0; uhol <= 360; uhol+=10) {
		 * Bressenham(sx,
		 * sy,(int)(sx+r*Math.cos(Math.toRadians(uhol))),(int)(sy+r*Math.sin(Math.
		 * toRadians(uhol)))); }
		 */
		/*
		 * glPointSize(10); glBegin(GL_POINTS); glColor3d(0, 0, 1); midpoint(180, 160,
		 * 60); glColor3d(0, 0, 0); midpoint(320, 160, 60); glColor3d(1, 0, 0);
		 * midpoint(460,160, 60); glColor3d(0, 1, 0); midpoint(390,220, 60);
		 * glColor3d(1, 1, 0); midpoint(250,220, 60); glEnd();
		 */
		// Bressenham(300, 300, 350, 360);
		//useckaDDA(100, 100, 200, 100);
		glBegin(GL_POINTS);
		hodinky();
		glEnd();
	}

	void loop() {
		glViewport(0, 0, width, height);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-0.5, width - 0.5, height - 0.5, -0.5, 0, 1);

		glShadeModel(GL_FLAT);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		// rgb (0,0,0) je cierne pozadie
		glClearColor(0.f, 0.f, 0.f, 1.f); // Initialize clear color

		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			vykresliGL();

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	public static void main(String[] args) {
		new cv01().spusti();
	}
}