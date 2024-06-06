import java.awt.*;

public class S2 {
    V2 origo = new V2(0,0);
    V2 O;
    M2 S;//scale
    M2 F;//flip
    M2 T;

    S2(int Sx, int Sy, int Ox, int Oy){
        O = new V2(Ox,Oy);
        S = new M2(Sx,0,
                 0, Sy);
        F= new M2(1, 0,
                0, -1);
        T=F.mul(S);
    }

    V2 transform(V2 v){
        return T.mul(v).add(O);
    }

    void drawLine(Graphics g, V2 p1, V2 p2){
        V2 pp1 = transform(p1);
        V2 pp2 = transform(p2);
        g.drawLine((int)pp1.x,(int)pp1.y,(int)pp2.x,(int)pp2.y);
    }


    void drawAxis(Graphics g){
        drawLine(g,origo,new V2(1,0));
        drawLine(g,origo,new V2(0,1));
    }


    void drawTriangle(Graphics g, V2 p1, V2 p2, V2 p3){
        drawLine(g,p1,p2);
        drawLine(g,p2,p3);
        drawLine(g,p3,p1);
        write(g,p1.twoPointVector(p2).angle(p1.twoPointVector(p3))+"deg",transform(p1));
        write(g,p2.twoPointVector(p1).angle(p2.twoPointVector(p3))+"deg",transform(p2));
        write(g,p3.twoPointVector(p1).angle(p3.twoPointVector(p2))+"deg",transform(p3));
    }

    void write(Graphics g, String str, V2 p){
        g.drawString(str,(int)p.x,(int)p.y);
    }


    public void drawPolygon(Graphics g, V2 center, double radius, double sides) {
        double angleIncrement = 2 * Math.PI / sides;
        double currentAngle = 0;
        drawShape(angleIncrement,currentAngle,center,g,sides,radius);
    }



    void drawStar(V2 center, double radius, Graphics g, double sides){//even / uneven
        double angleIncrement = 4 * Math.PI / sides;
        double currentAngle = Math.PI / 2;
        drawShape(angleIncrement,currentAngle,center,g,sides,radius);
    }

    void drawStarV2(Graphics g, int arms, double size, V2 center){
        int N = arms;
        V2[] points = new V2[N];
        double v = 2*Math.PI/N;

        for (int i = 0; i < N ; i++) {
            double x = Math.cos(i*v)*size;
            double y = Math.sin(i*v)*size;
            points[i] = new V2(y+center.x,x+center.y);//reverse y and y to center the star
        }

        for (int i = 0;i<N;i++){
            int j = (i+N/2)%N;
            if (N % 2 == 0) {//if even increment j by 1 and modulo N
                j = (j + 1) % N;
            }
            drawLine(g,points[i],points[j]);
        }
    }



    void drawShape(double angleIncrement, double currentAngle, V2 center, Graphics g, double sides, double radius){
        V2 lastPoint = new V2(center.x + radius * Math.cos(currentAngle),
                center.y + radius * Math.sin(currentAngle));

        for (int i = 0; i < sides; i++) {
            currentAngle += angleIncrement;
            V2 currentPoint = new V2(center.x + radius * Math.cos(currentAngle),
                    center.y + radius * Math.sin(currentAngle));

            drawLine(g, lastPoint, currentPoint);
            lastPoint = currentPoint;

        }
    }

    void drawPoint(Graphics g,V2 p){
        V2 pp = transform(p);
        g.fillOval((int)pp.x-1,(int)pp.y-1, 2,2);
    }

    void drawPoint(Graphics g,V2 p,int size){
        V2 pp = transform(p);
        g.fillOval((int)pp.x-1,(int)pp.y-1, size,size);
    }

    void drawPoint(Graphics g,V2 p, Color c, int size){
        V2 pp = transform(p);
        g.setColor(c);
        g.fillOval((int)pp.x-1,(int)pp.y-1, size,size);
        g.setColor(Color.BLACK);

    }

    void drawCircle(Graphics g, int radius,V2 center){

        for (double v = 0; v < 2*Math.PI; v+=0.001) {
                double x = center.x + radius * Math.cos(v);
                double y = center.y + radius * Math.sin(v);
                drawPoint(g,new V2(x,y));
            }
    }
    void drawElipse(Graphics g, V2 p0, double a, double b){

//        V2 p0 = new V2(4,3);//Centrum for ellipse (x0,y0)
//        double a = 3;//store akse
//        double b = 2;//lille akse

        for (double v = 0; v < 2 * Math.PI; v += 0.01) { //vinkel er parameter
            V2 p = p0.add(new V2(a*Math.cos(v),b*Math.sin(v)));// punkter på ellipsen   // parameterfremstilling for elipse
            drawPoint(g,p);
        }
    }

//    V2 transform(V2 v){         // Transform from 2D virtual world coordinates to pixels
//        return T.mul(v).add(O);
//    }

    public void moveTo(V2 p){
        O=transform(p);
    }

    public void rotate(double phi){
        M2 R=new M2(Math.cos(phi), -Math.sin(phi),
                Math.sin(phi), Math.cos(phi));
        T=T.mul(R);
    }

    void drawRect(Graphics g, V2 a, V2 b,V2 c, V2 d ){
        drawLine(g,a,b);
        drawLine(g,b,c);
        drawLine(g,c,d);
        drawLine(g,d,a);
    }
    void drawRect(Graphics g, V2 topLeftCorner, double size) {
        V2 transformedCorner = transform(topLeftCorner);
        size *= S.a;
        g.drawRect((int)transformedCorner.x, (int)transformedCorner.y, (int)size, (int)size);
    }
    public void fillRect(Graphics g, V2 topLeftCorner,int size,int scale, Color c){
        g.setColor(c);
        V2 transformedCorner = transform(topLeftCorner);
        size = size*scale;
        g.fillRect((int)transformedCorner.x,(int)transformedCorner.y,size,size);
        g.setColor(Color.BLACK);
    }

    public void drawImage(Graphics g, V2 p, int size, String imgUrl){
        Image img = Toolkit.getDefaultToolkit().getImage(imgUrl);
        V2 pp = transform(p);
        g.drawImage(img,(int)pp.x, (int)pp.y, size, size,null);
    }

//    public V2 inverseTransform(V2 v) {
//        //unflip
//        V2 flipped = F.mul(v);
//
//        // Undo the scaling operation
//        V2 scaled = new V2(flipped.x / S.a, flipped.y / S.d);
//
//        // Undo the translation operation
//        V2 original = scaled.add(O);
//
//        return original;
//    }

    //cirkel
    //semicirkel
    //prik
    //firkant
    //smiley
    //histogram-søjler???
    //ur




}
