import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {
    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject jsonObject = new JSONObject(content);

            int n = jsonObject.getJSONObject("keys").getInt("n");
            int k = jsonObject.getJSONObject("keys").getInt("k");

            List<Point> points = new ArrayList<>();
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject pointObject = jsonObject.getJSONObject(key);
                    int base = pointObject.getInt("base");
                    String value = pointObject.getString("value");
                    int y = decodeValue(value, base);
                    int x = Integer.parseInt(key);
                    points.add(new Point(x, y));
                }
            }
            if (points.size() < k) {
                System.out.println("Not enough points to determine the polynomial.");
                return;
            }
            double c = lagrangeInterpolation(points, 0);
            System.out.println(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    private static double lagrangeInterpolation(List<Point> points, int x) {
        double total = 0.0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            Point pi = points.get(i);
            double term = pi.y;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Point pj = points.get(j);
                    term *= (double) (x - pj.x) / (pi.x - pj.x);
                }
            }
            total += term;
        }
        return total;
    }

    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
