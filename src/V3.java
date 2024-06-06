public class V3 {
    double x,y,z;


    V3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    V3 add(V3 v) {
        return new V3(x + v.x, y + v.y, z + v.z);
    }

    V3 sub(V3 v) {
        return new V3(x - v.x, y - v.y, z - v.z);
    }

    V3 mul(double d) {
        return new V3(d * x, d * y, d * z);
    }

    double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    V3 unit() {
        double l = length();
        return new V3(x / l, y / l, z / l);
    }

    double dot(V3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    V3 cross(V3 v) {
        return new V3(y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public String toString() {
        return "("+x+","+y+","+z+")";
    }

    public static void main(String[] args) {
        //test

    }
}