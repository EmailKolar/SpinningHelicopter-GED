import java.awt.*;
import java.awt.event.KeyEvent;

public class Camera {
    V3 O = new V3(0,0,0); //virtual world basis
    V3 i = new V3(1,0,0);
    V3 j = new V3(0,1,0);
    V3 k = new V3(0,0,1);

    V3 E = new V3(0,0,0); // Camera basis
    V3 D = new V3(1,0,0);
    V3 U = new V3(0,1,0);
    V3 R = new V3(0,0,1);
    double z = 2;

    S2 s2;
    V3 cameraPosition;
    M3 I = new M3(1,0,0,
            0,1,0,
            0,0,1);
    M3 Sz = new M3(0,-1,0,
            1,0,0,
            0,0,0);
    M3 Sy = new M3(0,0,1,
            0,0,0,
            -1,0,0);
//    M3 Sx = new M3(0,0,0,
//            0,0,-1)

    Camera(int sx, int sy, int ox, int oy){
        s2=new S2(sx,sy,ox,oy);
        cameraPosition = new V3(10, 10, 0);
    }

    V2 project(V3 p){
        V3 EP = p.sub(E);
        double d = D.dot(EP);
        double u = U.dot(EP);
        double r = R.dot(EP);
        if(d <= 0) return null;
        double rm = r*z/d;
        double um = u*z/d;

        return new V2(rm,um);
    }

    void moveTo(V3 p){
        E = new V3(p.x, p.y, p.z);
    }

    void focus(V3 p){
        D = p.sub(E).unit();
        R = D.cross(k).unit();
        U = R.cross(D);
    }
    void zoom(double zoomFactor){
        z*=zoomFactor;
    }


    void yaw(double angle){
        double phi = Math.PI/angle;
        M3 Rz = I.add(Sz.mul(Math.sin(phi))).add(Sz.mul(Sz).mul(1-Math.cos(phi)));//rotations matrix
        D = Rz.mul(D);
        U = R.cross(D);
        R = D.cross(U).unit();
    }
    void pitch(double angle){
        double phi = Math.PI/angle;
        M3 Ry = I.add(Sy.mul(Math.sin(phi))).add(Sy.mul(Sy).mul(1-Math.cos(phi)));//rotations matrix
        D = Ry.mul(D);
        U = Ry.mul(U);
        //R = D.cross(U).unit();
    }
    void roll(double angle){

    }

    void drawAxis(Graphics g){
        drawLine(g,O,i);
        drawLine(g,O,j);
        drawLine(g,O,k);
    }
    void drawPoint(Graphics g, V3 p){
        s2.drawPoint(g,project(p));
    }
    void drawPoint(Graphics g, V3 p,int size){
        s2.drawPoint(g,project(p),size);
    }

    void drawLine(Graphics g, V3 p1, V3 p2){
        V2 pp1 = project(p1);
        V2 pp2 = project(p2);
        if (pp1 == null|| pp2==null)return;
        s2.drawLine(g,pp1,pp2);
    }

    void drawImg(Graphics g, String imgUrl, V3 p){
        //public void drawImage(Graphics g, V2 p, int size, String imgUrl){
        s2.drawImage(g,project(p),10,imgUrl);

    }
    void drawWall(Graphics g,V3 corner, int width, int height, Color c){
        V3[] vertices = {
                corner,
                new V3(corner.x, corner.y-height,corner.z),
                new V3(corner.x+width, corner.y-height,corner.z),
                new V3(corner.x+width, corner.y,corner.z)

        };
        V2[] transformedVertices = new V2[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            transformedVertices[i] = s2.transform(project(vertices[i]));
        }
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        for (int l = 0; l < transformedVertices.length; l++) {
            xPoints[l]=(int)transformedVertices[l].x;
            yPoints[l]=(int)transformedVertices[l].y;
        }
        g.setColor(c);
        g.fillPolygon(xPoints,yPoints,4);
    }

    V3[] getCube(int size, V3 center){
        double halfSize = size / 2.0;

        // Define the vertices of the cube
        V3[] vertices = {
                new V3(center.x - halfSize, center.y - halfSize, center.z - halfSize),
                new V3(center.x + halfSize, center.y - halfSize, center.z - halfSize),
                new V3(center.x + halfSize, center.y + halfSize, center.z - halfSize),
                new V3(center.x - halfSize, center.y + halfSize, center.z - halfSize),
                new V3(center.x - halfSize, center.y - halfSize, center.z + halfSize),
                new V3(center.x + halfSize, center.y - halfSize, center.z + halfSize),
                new V3(center.x + halfSize, center.y + halfSize, center.z + halfSize),
                new V3(center.x - halfSize, center.y + halfSize, center.z + halfSize)
        };
        return vertices;
    }

    void drawCube(Graphics g, V3 center, int size,Color c) {
        // Define the vertices of the cube
        V3[] vertices = getCube(size,center);
        drawCube(g,vertices,c);
    }

    void drawCube(Graphics g, V3[] vertices, Color c){
        // Apply transformation to each vertex
        V2[] transformedVertices = new V2[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            V2 p = project(vertices[i]);
            if(p == null)return;
            transformedVertices[i] = s2.transform(p);
        }

        // Define the faces of the cube using polygons
        int[][] faces = {
                {0, 1, 2, 3}, // Front face
                {4, 5, 6, 7}, // Back face
                {0, 1, 5, 4}, // Top face
                {2, 3, 7, 6}, // Bottom face
                {1, 2, 6, 5}, // Right face
                {0, 3, 7, 4}  // Left face
        };

        // Draw the faces of the cube
        for (int[] face : faces) {
            int[] xPoints = new int[4];
            int[] yPoints = new int[4];
            for (int i = 0; i < 4; i++) {
                xPoints[i] = (int) transformedVertices[face[i]].x;
                yPoints[i] = (int) transformedVertices[face[i]].y;
            }
            g.setColor(c); // Set the color for the cube faces
            g.fillPolygon(xPoints, yPoints, 4); // Draw the polygon representing a face
        }

        g.setColor(Color.black);
        // Draw the edges of the cube
        drawLine(g, vertices[0], vertices[1]);
        drawLine(g, vertices[1], vertices[2]);
        drawLine(g, vertices[2], vertices[3]);
        drawLine(g, vertices[3], vertices[0]);

        drawLine(g, vertices[4], vertices[5]);
        drawLine(g, vertices[5], vertices[6]);
        drawLine(g, vertices[6], vertices[7]);
        drawLine(g, vertices[7], vertices[4]);

        drawLine(g, vertices[0], vertices[4]);
        drawLine(g, vertices[1], vertices[5]);
        drawLine(g, vertices[2], vertices[6]);
        drawLine(g, vertices[3], vertices[7]);

        Polygon polygon;

    }

    void drawEdgeCube(Graphics g, V3[] vertices){
        g.setColor(Color.black);
        // Draw the edges of the cube
        drawLine(g, vertices[0], vertices[1]);
        drawLine(g, vertices[1], vertices[2]);
        drawLine(g, vertices[2], vertices[3]);
        drawLine(g, vertices[3], vertices[0]);

        drawLine(g, vertices[4], vertices[5]);
        drawLine(g, vertices[5], vertices[6]);
        drawLine(g, vertices[6], vertices[7]);
        drawLine(g, vertices[7], vertices[4]);

        drawLine(g, vertices[0], vertices[4]);
        drawLine(g, vertices[1], vertices[5]);
        drawLine(g, vertices[2], vertices[6]);
        drawLine(g, vertices[3], vertices[7]);
    }
    void drawEdgeCube(Graphics g, int size, V3 center){
        double halfSize = size / 2.0;

        // Define the vertices of the cube
        V3[] vertices = {
                new V3(center.x - halfSize, center.y - halfSize, center.z - halfSize),
                new V3(center.x + halfSize, center.y - halfSize, center.z - halfSize),
                new V3(center.x + halfSize, center.y + halfSize, center.z - halfSize),
                new V3(center.x - halfSize, center.y + halfSize, center.z - halfSize),
                new V3(center.x - halfSize, center.y - halfSize, center.z + halfSize),
                new V3(center.x + halfSize, center.y - halfSize, center.z + halfSize),
                new V3(center.x + halfSize, center.y + halfSize, center.z + halfSize),
                new V3(center.x - halfSize, center.y + halfSize, center.z + halfSize)
        };
        g.setColor(Color.black);
        // Draw the edges of the cube
        drawLine(g, vertices[0], vertices[1]);
        drawLine(g, vertices[1], vertices[2]);
        drawLine(g, vertices[2], vertices[3]);
        drawLine(g, vertices[3], vertices[0]);

        drawLine(g, vertices[4], vertices[5]);
        drawLine(g, vertices[5], vertices[6]);
        drawLine(g, vertices[6], vertices[7]);
        drawLine(g, vertices[7], vertices[4]);

        drawLine(g, vertices[0], vertices[4]);
        drawLine(g, vertices[1], vertices[5]);
        drawLine(g, vertices[2], vertices[6]);
        drawLine(g, vertices[3], vertices[7]);
    }

    public void handleArrowKeys(KeyEvent e) {
        int keyCode = e.getKeyCode();
        double stepSize = 0.5; //
        double angleStep = 100; //

        switch(keyCode) {
            case KeyEvent.VK_W:
                moveForward(stepSize);
                break;
            case KeyEvent.VK_S:
                moveBackward(stepSize);
                break;
            case KeyEvent.VK_A:
                strafeLeft(stepSize);
                break;
            case KeyEvent.VK_D:
                strafeRight(stepSize);
                break;
            case KeyEvent.VK_UP:
                pitch(-angleStep);
                break;
            case KeyEvent.VK_DOWN:
                pitch(angleStep);
                break;
            case KeyEvent.VK_LEFT:
                yaw(angleStep);
                break;
            case KeyEvent.VK_RIGHT:
                yaw(-angleStep);
                break;
        }
    }





    public void moveForward(double distance) {
        // Move the camera forward along its facing direction
        V3 movement = D.mul(distance);
        E = E.add(movement);
    }

    public void moveBackward(double distance) {
        // Move the camera backward along its facing direction
        V3 movement = D.mul(-distance); // Negative distance to move backward
        E = E.add(movement);
    }

    public void strafeLeft(double distance) {
        // Strafe the camera to the left (perpendicular to its facing direction)
        V3 strafeDirection = R.mul(-distance); // Negative distance to move left
        E = E.add(strafeDirection);
    }

    public void strafeRight(double distance) {
        // Strafe the camera to the right (perpendicular to its facing direction)
        V3 strafeDirection = R.mul(distance);
        E = E.add(strafeDirection);
    }


//    public void setFocusFromMouseClick(int mouseX, int mouseY) {
//        // Transform mouse coordinates to world coordinates
//        V2 worldCoordinates = s2.inverseTransform(new V2(mouseX, mouseY));
//        V3 wC = new V3(worldCoordinates.x, worldCoordinates.y, 0);
//        // Update camera focus position
//        focus(wC);
//    }


}

