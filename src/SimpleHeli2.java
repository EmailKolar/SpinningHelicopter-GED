import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SimpleHeli2 extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new SimpleHeli2();
        frame.setSize(700, 700);
        frame.setTitle("Helicopter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    public SimpleHeli2() {
        add(new DrawPanel());
    }

    class DrawPanel extends JPanel {
        Timer myTimer = new Timer(50, new TimerListener());  // Timer updates every 50 ms
        Camera camera = new Camera(200, 200, 500, 300);

        ArrayList<V3[]> rorKasser = new ArrayList<>();

        double phi = Math.PI/100;

        M3 I = new M3(1,0,0,
                0,1,0,
                0,0,1);
        M3 Sz = new M3(0,-1,0,
                1,0,0,
                0,0,0);
        M3 Rz = I.add(Sz.mul(Math.sin(phi))).add(Sz.mul(Sz).mul(1-Math.cos(phi)));//rotations matrix




        DrawPanel() {
            camera.moveTo(new V3(20, 3, 8));
            camera.focus(new V3(0, 0, 0));

            setFocusable(true); // Set the JPanel focusable
            requestFocusInWindow(); // Request focus for the JPanel

             rorKasser.add(camera.getCube(1,new V3(1,1,3)));
             rorKasser.add(camera.getCube(1,new V3(1,2,3)));
             rorKasser.add(camera.getCube(1,new V3(1,3,3)));
             rorKasser.add(camera.getCube(1,new V3(1,4,3)));
             rorKasser.add(camera.getCube(1,new V3(1,0,3)));
             rorKasser.add(camera.getCube(1,new V3(1,-1,3)));
             rorKasser.add(camera.getCube(1,new V3(1,-2,3)));


            myTimer.start(); // Start the timer only once in the constructor
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            camera.drawAxis(g);
            //FØRSTE LAG
            camera.drawEdgeCube(g, 1, new V3(0,0,0));
            camera.drawEdgeCube(g, 1, new V3(0,1,0));
            camera.drawEdgeCube(g, 1, new V3(0,2,0));
            camera.drawEdgeCube(g, 1, new V3(0,3,0));

            camera.drawEdgeCube(g, 1, new V3(1,0,0));
            camera.drawEdgeCube(g, 1, new V3(1,1,0));
            camera.drawEdgeCube(g, 1, new V3(1,2,0));
            camera.drawEdgeCube(g, 1, new V3(1,3,0));

            camera.drawEdgeCube(g, 1, new V3(2,0,0));
            camera.drawEdgeCube(g, 1, new V3(2,1,0));
            camera.drawEdgeCube(g, 1, new V3(2,2,0));
            camera.drawEdgeCube(g, 1, new V3(2,3,0));
            //ANDET LAG
            camera.drawEdgeCube(g, 1, new V3(0,0,1));
            camera.drawEdgeCube(g, 1, new V3(0,1,1));
            camera.drawEdgeCube(g, 1, new V3(0,2,1));
            camera.drawEdgeCube(g, 1, new V3(0,3,1));

            camera.drawEdgeCube(g, 1, new V3(1,0,1));
            camera.drawEdgeCube(g, 1, new V3(1,1,1));
            camera.drawEdgeCube(g, 1, new V3(1,2,1));
            camera.drawEdgeCube(g, 1, new V3(1,3,1));

            camera.drawEdgeCube(g, 1, new V3(2,0,1));
            camera.drawEdgeCube(g, 1, new V3(2,1,1));
            camera.drawEdgeCube(g, 1, new V3(2,2,1));
            camera.drawEdgeCube(g, 1, new V3(2,3,1));


            //STANG
            camera.drawEdgeCube(g, 1, new V3(1,1,2));


            //ROR
//            camera.drawEdgeCube(g, 1, new V3(1,1,3));
//
//            camera.drawEdgeCube(g, 1, new V3(1,2,3));
//            camera.drawEdgeCube(g, 1, new V3(1,3,3));
//            camera.drawEdgeCube(g, 1, new V3(1,4,3));
//
//            camera.drawEdgeCube(g, 1, new V3(1,0,3));
//            camera.drawEdgeCube(g, 1, new V3(1,-1,3));
//            camera.drawEdgeCube(g, 1, new V3(1,-2,3));

            for (int i = 0; i < rorKasser.size(); i++) {
                for (int j = 0; j < rorKasser.get(i).length; j++) {
                    rorKasser.get(i)[j]=Rz.mul(rorKasser.get(i)[j].sub(new V3(1,1,3))).add(new V3(1,1,3));
                }
            }

            for (V3[] v3s : rorKasser) {
                camera.drawEdgeCube(g, v3s);
            }





        }

        class TimerListener implements ActionListener {
            public void actionPerformed(ActionEvent evt) {
                repaint();
            }
        }
    }
}
