import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RotatingHelicopter extends JFrame {
    public static void main(String[] args) {
        RotatingHelicopter frame=new RotatingHelicopter();
        frame.setTitle("AttackHeli");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } // main()

    RotatingHelicopter() {
        add(new PaintPanel());
    }
  
    class PaintPanel extends JPanel {
       Camera camera = new Camera(200,200,500,300);
       WaveFrontElement wf = new WaveFrontElement("src/wavefrontObjects/Warsong Attack Helicopter.obj");

        Timer myTimer=new Timer(50, new TimerListener());  // Timer updates every 50 ms


        V3 center =wf.center(wf.points);
        V3 uv = new V3(0,0,1).unit(); //unitVector for rotation

        M3 Su = new M3(
                0,-uv.z,uv.y,
                uv.z,0,-uv.x,
                -uv.y, uv.x, 0);
        M3 I = new M3(
                1,0,0,
                0,1,0,
                0,0,1);

        M3 Sz = new M3(
                0,-1,0,
                1,0,0,
                0,0,0);


        double phi = Math.PI/100;
        M3 Rz = I.add(Sz.mul(Math.sin(phi))).add(Sz.mul(Sz).mul(1-Math.cos(phi)));//rotations matrix
        M3 Ru = I.add(Su.mul(Math.sin(phi))).add(Su.mul(Su).mul(1-Math.cos(phi)));

        ArrayList<V3> bladeVertices = new ArrayList<>();
        ArrayList<V3> bodyVertices = new ArrayList<>();
        ArrayList<Integer> bladeVertexIndices = new ArrayList<>();
        ArrayList<Integer> bodyVertexIndices = new ArrayList<>();
        ArrayList<WaveFrontElement.Face> bladeFaces = new ArrayList<>();
        ArrayList<WaveFrontElement.Face> bodyFaces = new ArrayList<>();
        V3 bladeCenter;
        V3 bodyCenter;


        PaintPanel() {
            camera.moveTo(new V3(17,10,2));
            camera.zoom(1);

//            center = wf.center(wf.points);
            camera.focus(center);
            myTimer.start();    // Start simulation

            setFocusable(true);
            requestFocusInWindow();
            addMouseMotionListener(new MyMouseMotionListener());

            System.out.println(center);

            for (int i = 1; i < wf.points.size(); i++) {
                if(wf.points.get(i).z>=center.z+1.75 && wf.points.get(i).y<=center.y+6.3692825){
                    bladeVertexIndices.add(i);
                    bladeVertices.add(wf.points.get(i));
                }else {
                    bodyVertices.add(wf.points.get(i));
                    bodyVertexIndices.add(i);
                }
            }
            for (int i = 0; i < wf.faces.size(); i++) {
                if(bladeVertexIndices.contains(wf.faces.get(i).a)|| bladeVertexIndices.contains(wf.faces.get(i).b)|| bladeVertexIndices.contains(wf.faces.get(i).c)){
                    bladeFaces.add(wf.faces.get(i));
                }
            }
            for (int i = 0; i < wf.faces.size(); i++) {
                if(!bladeFaces.contains(wf.faces.get(i))){
                    bodyFaces.add(wf.faces.get(i));
                }
            }

            bladeCenter = wf.center(bladeVertices);
            bodyCenter = wf.center(bodyVertices);

        }





        class MyMouseMotionListener extends MouseMotionAdapter{
           int oldX=0;
           int oldY=0;
           public void mouseDragged(MouseEvent e){
               int x=e.getX();
               int y = e.getY();
               int dx = x-oldX;
               int dy = -(y-oldY);
               if(dx==0&&dy==0) return;

               V3 v = camera.R.mul(dx).add(camera.U.mul(dy));
               V3 u = v.cross(camera.D).unit();

               for (int i = 0; i < bodyVertices.size(); i++) { //rotate body
                   V3 rot = bodyVertices.set(i,wf.rotate(u,bodyVertices.get(i),center));
                   wf.points.set(bodyVertexIndices.get(i),rot);
               }


               uv = wf.rotate(u,uv, new V3(0,0,0));//rotate blade rot axis
               Su = new M3(
                       0,-uv.z,uv.y,
                       uv.z,0,-uv.x,
                       -uv.y, uv.x, 0);
               Ru = I.add(Su.mul(Math.sin(phi))).add(Su.mul(Su).mul(1-Math.cos(phi)));//make new rot matrix for blades
               for (int i = 0; i < bladeVertices.size(); i++) {
                   V3 rotated = bladeVertices.set(i,wf.rotate(u,bladeVertices.get(i),center));
                   wf.points.set(bladeVertexIndices.get(i),rotated);
               }

               repaint();
               oldX=x;
               oldY=y;

           }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            camera.focus(center);
            wf.drawPart(camera, g, bodyFaces);
            camera.drawAxis(g);

//            g.setColor(Color.RED);
//            camera.drawLine(g,center,new V3(center.x, center.y,center.z+1.75));
//            camera.drawLine(g,new V3(center.x, center.y,center.z+1.75),new V3(center.x, center.y+6.4,center.z+1.75));
//            g.setColor(Color.BLACK);

            bladeCenter = wf.center(bladeVertices);

            for (int i = 0; i < bladeVertices.size(); i++) {
                V3 rotatedBladeVertex = Ru.mul(bladeVertices.get(i).sub(bladeCenter)).add(bladeCenter);
                wf.points.set(bladeVertexIndices.get(i),rotatedBladeVertex);
                bladeVertices.set(i,rotatedBladeVertex);
            }

            wf.drawPart(camera, g, bladeFaces);

        }
        class TimerListener implements ActionListener {
            public void actionPerformed(ActionEvent evt){
                repaint();
            }
        }

    } // class PaintPanel

} // class MainFrame