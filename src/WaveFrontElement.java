import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WaveFrontElement {

    class Face {
        int a, b, c;
        Face(int a, int b, int c){
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    ArrayList<V3> points = new ArrayList<>();
    ArrayList<Face> faces = new ArrayList<>();

    V3 initCenter;


    WaveFrontElement(String src){
        points.add(new V3(0,0,0));
        readFile(src);
        initCenter = center(points);

    }

    void readFile(String filename){
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (fileScanner.hasNextLine()){
            String text = fileScanner.nextLine();

            if (text.startsWith("v ")) {
                String[] parts = text.trim().split("\\s+");
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                points.add(new V3(x,z,y));
            }
            if(text.startsWith("f")){
                String[] parts = text.trim().split("\\s+");
                int x = Integer.parseInt(parts[1].split("/")[0]);
                int y = Integer.parseInt(parts[2].split("/")[0]);
                int z = Integer.parseInt(parts[3].split("/")[0]);
                faces.add(new Face(x, y, z));
            }
        }
        fileScanner.close();
    }

    void draw(Camera c, Graphics g){
        for (int i = 0; i < faces.size(); i++) {
            c.drawLine(g,points.get(faces.get(i).a),points.get(faces.get(i).b));
            c.drawLine(g,points.get(faces.get(i).b),points.get(faces.get(i).c));
            c.drawLine(g,points.get(faces.get(i).c),points.get(faces.get(i).a));
        }
    }
    void drawW(Camera c, Graphics g, ArrayList<Face> facesB){
        for (int i = 0; i < facesB.size(); i++) {
            c.drawLine(g,points.get(facesB.get(i).a),points.get(facesB.get(i).b));
            c.drawLine(g,points.get(facesB.get(i).b),points.get(facesB.get(i).c));
            c.drawLine(g,points.get(facesB.get(i).c),points.get(facesB.get(i).a));
        }
    }

    void draw(Camera c, Graphics g, ArrayList<V3> rpoints){
        for (int i = 0; i < faces.size(); i++) {
            V3 Pa =points.get(faces.get(i).a);
            V3 Pb =points.get(faces.get(i).b);
            V3 Pc =points.get(faces.get(i).c);
            for (int j = 0; j < rpoints.size(); j++) {
                if(Pa != rpoints.get(j)){
                    c.drawLine(g,Pa,Pb);
                    c.drawLine(g,Pb,Pc);
                    c.drawLine(g,Pc,Pa);
                }
            }
        }
    }

    V3 center(ArrayList<V3> points) {
        double  sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        int count = points.size();

        for (V3 point : points) {
            sumX += point.x;
            sumY += point.y;
            sumZ += point.z;
        }

        double centerX = sumX / count;
        double centerY = sumY / count;
        double centerZ = sumZ / count;

        return new V3(centerX, centerY, centerZ);
    }

    void rotate(M3 Rz){
        V3 center = center(points);
        for (int i = 0; i < points.size(); i++) {
            V3 hej = Rz.mul(points.get(i).sub(center)).add(center);
            points.set(i,hej);
        }
    }

    M3 I = new M3(1,0,0,
            0,1,0,
            0,0,1);

    void rotate(V3 u){
        double phi = Math.PI/100;
        M3 Su =new M3 (0,-u.z,u.y,
        u.z,0,-u.x,
        -u.y,u.x,0);
        M3 Ru = I.add(Su.mul(Math.sin(phi))).add(Su.mul(Su).mul(1-Math.cos(phi)));//rotations matrix
//        System.out.println(Ru);
        V3 center = center(points);
        //V3 pos = new V3(10,5,2);
        for (int i = 0; i < points.size(); i++) {
            V3 hej = Ru.mul(points.get(i).sub(center)).add(center);
            points.set(i,hej);
        }
    }
    V3 rotate(V3 u, V3 vec){ //TODO TEST NOTE THIS IS FOR ROTATION ROTATION AXIS FOR HELI
        double phi = Math.PI/100;
        M3 Su =new M3 (0,-u.z,u.y,
                u.z,0,-u.x,
                -u.y,u.x,0);
        M3 Ru = I.add(Su.mul(Math.sin(phi))).add(Su.mul(Su).mul(1-Math.cos(phi)));//rotations matrix
        vec = Ru.mul(vec.sub(new V3(0,0,0)).add(new V3(0,0,0)));
        return vec;
    }
    V3 rotate(V3 u, V3 vec, V3 bCenter){ //TODO TEST NOTE THIS IS FOR ROTATION ROTATION AXIS FOR HELI
        double phi = Math.PI/100;
        M3 Su =new M3 (0,-u.z,u.y,
                u.z,0,-u.x,
                -u.y,u.x,0);
        M3 Ru = I.add(Su.mul(Math.sin(phi))).add(Su.mul(Su).mul(1-Math.cos(phi)));//rotations matrix
        vec = Ru.mul(vec.sub(bCenter).add(bCenter));
        return vec;
    }



//
//    void run(){
//        String path = "src/wavefrontObjects/utha_teapot.obj";
//        ArrayList<String> test = new ArrayList<>();
//
//        readFile(path);
//        System.out.println(points.get(1));
//        System.out.println(faces.get(0));
//        faces.get(1).
//
//    }
//
//    public static void main(String[] args) {
//
//
//        new WaveFrontElement("").run();
//    }



}
