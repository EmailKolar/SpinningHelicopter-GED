public class V2 {
    double x, y;

    V2(double x, double y){
        this.x = x;
        this.y = y;
    }
    V2 add(V2 v){//vektor plus vektor
        return new V2(x+v.x,y+v.y);
    }
    V2 sub(V2 v){//vektor minus vektor
        return new V2(x-v.x,y-v.y);
    }
    V2 mul(double num){//gang vektor med tal
        return new V2(x*num,y*num);
    }
    double dot(V2 v){//skalarprodukt
        return x*v.x+y*v.y;
    }
    double length(){//længde af vektor
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }
    double angle(V2 v){
        return Math.toDegrees(Math.acos(dot(v)/(length()*v.length())));
    }

    V2 twoPointVector(V2 v){
        return new V2(v.x-x,v.y-y);
    }

    V2 div(double num){
        return new V2(x/num,y/num);
    }

    V2 unit(){//makes unit length vector of vector
        return div(length());
    }


    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    public static void main(String[] args) {

//        V2 v3 = v1.add(v2);
//        System.out.println("v1 = "+v1);
//        System.out.println("v2 = "+v2);
//        System.out.println("v3 = "+v3);
        V2 v1 = new V2(4,-3);
        V2 v2 = new V2(4,2);
        System.out.println(v1.angle(v2));

    }



}
