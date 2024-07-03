package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DelaunayTriangulation {


    static class Point2D {
        double x, y;

        Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    static class Triangle {
        Point2D p1, p2, p3;

        Triangle(Point2D p1, Point2D p2, Point2D p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        void printPoints() {
            System.out.println("p1 = " + p1 + " p2 = " + p2 + " p3 = " + p3);
        }

        public String toString() {
            return ("p1 = " + p1 + " p2 = " + p2 + " p3 = " + p3);
        }

    }
    public static Point2D findMAXPoint(List<Point2D> points) {
        if (points.isEmpty()) {
            return null;
        }

        double maxX = points.get(0).getX();
        double maxY = points.get(0).getY();

        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            double x = point.getX();
            double y = point.getY();
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        return new Point2D(maxX, maxY);
    }

    public static Point2D findMinPoint(List<Point2D> points) {
        if (points.isEmpty()) {
            return null;
        }

        double minX = points.get(0).getX();
        double minY = points.get(0).getY();


        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            double x = point.getX();
            double y = point.getY();
            if (x < minX) {
                minX = x;
            }
            if (y < minY) {
                minY = y;
            }
        }

        return new Point2D(minX, minY);
    }





    public static List<Triangle> delaunayTriangulation(List<Point2D> points) {
        List<Triangle> triangles = new ArrayList<>();

        // Добавляем начальный треугольник, содержащий все точки
        System.out.println("len = " + points.size());
        double x_min = findMinPoint(points).getX() ;
        double y_min = findMinPoint(points).getY();
        double maxX = findMAXPoint(points).getX() ;
        double maxY = findMAXPoint(points).getY() ;
        Point2D superPoint1 = new Point2D(x_min, y_min);
        Point2D superPoint2 = new Point2D(0, maxX + maxY);
        Point2D superPoint3 = new Point2D(maxX + maxY, y_min);
        Triangle superTriangle = new Triangle(superPoint1, superPoint2, superPoint3);
        triangles.add(superTriangle);


        for (Point2D point : points) {
            List<Triangle> badTriangles = new ArrayList<>();
            for (Triangle triangle : triangles) {
                if (isPointInsideCircumcircle(point, triangle)) {
                    badTriangles.add(triangle);
                }
                System.out.println("Adding triangles for point: " + point);
            }
            List<Edge> polygon = findBoundary(badTriangles);
            triangles.removeAll(badTriangles);
            for (Edge edge : polygon) {
                Triangle newTriangle = new Triangle(edge.p1, edge.p2, point);
                triangles.add(newTriangle);
            }

        }

        triangles.get(0).printPoints();
        //  Удаляем треугольники, содержащие вершины изначального треугольника
        triangles.removeIf(triangle ->
                triangle.p1 == superPoint1 || triangle.p1 == superPoint2 || triangle.p1 == superPoint3 ||
                        triangle.p2 == superPoint1 || triangle.p2 == superPoint2 || triangle.p2 == superPoint3 ||
                        triangle.p3 == superPoint1 || triangle.p3 == superPoint2 || triangle.p3 == superPoint3
        );


        return triangles;
    }


    static boolean isPointInsideCircumcircle(Point2D point, Triangle triangle) {

        double AC = Math.sqrt(Math.pow(triangle.p2.getX() - triangle.p1.getX(), 2) + Math.pow(triangle.p2.getY() - triangle.p1.getY(), 2));
        double AB = Math.sqrt(Math.pow(triangle.p3.getX() - triangle.p1.getX(), 2) + Math.pow(triangle.p3.getY() - triangle.p1.getY(), 2));
        double BC = Math.sqrt(Math.pow(triangle.p2.getX() - triangle.p3.getX(), 2) + Math.pow(triangle.p2.getY() - triangle.p3.getY(), 2));

        double p = (AC + AB + BC)/2;
        double S = Math.sqrt(p*(p-AB)*(p-BC)*(p-AC));
        double R = (AB*BC*AC)/(4*S);

        // Проверка условия нахождения внутри окружности
        return ((Math.pow(point.y - findCircleCenter(triangle.p1, triangle.p2, triangle.p3).y, 2))
                + (Math.pow(point.x - findCircleCenter(triangle.p1, triangle.p2, triangle.p3).x, 2)) <= R*R );

    }

    public static Point2D findCircleCenter(Point2D p1, Point2D p2, Point2D p3) {
        double x1 = p1.x;
        double y1 = p1.y;
        double x2 = p2.x;
        double y2 = p2.y;
        double x3 = p3.x;
        double y3 = p3.y;

        double A = x2 - x1;
        double B = y2 - y1;
        double C = x3 - x1;
        double D = y3 - y1;

        double E = ((x2*x2 - x1*x1) + (y2*y2 - y1*y1)) / 2;
        double F = ((x3*x3 - x1*x1) + (y3*y3 - y1*y1)) / 2;

        double x = (E*D - B*F) / (A*D - B*C);
        double y = (A*F - E*C) / (A*D - B*C);

        return new Point2D(x, y);
    }


    static List<Edge> findBoundary(List<Triangle> triangles) {
        List<Edge> edges = new ArrayList<>();
        for (Triangle triangle : triangles) {
            addEdge(edges, triangle.p1, triangle.p2);
            addEdge(edges, triangle.p2, triangle.p3);
            addEdge(edges, triangle.p3, triangle.p1);

        }
        List<Edge> boundary = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.count == 1) {
                boundary.add(edge);
            }
        }


        return boundary;
    }

    static void addEdge(List<Edge> edges, Point2D p1, Point2D p2) {
        Edge edge = new Edge(p1, p2);
        Edge reverseEdge = new Edge(p2, p1);
        int index = edges.indexOf(reverseEdge);
        if (index != -1) {
            edges.get(index).count--;
        } else {
            edges.add(edge);
        }
    }

    static class Edge {
        Point2D p1, p2;
        int count;

        Edge(Point2D p1, Point2D p2) {
            this.p1 = p1;
            this.p2 = p2;
            count = 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Edge) {
                Edge other = (Edge)obj;
                return (p1.equals(other.p1) && p2.equals(other.p2)) ||
                        (p1.equals(other.p2) && p2.equals(other.p1));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return p1.hashCode() + p2.hashCode();
        }
    }

    public static BufferedImage drawDelaunayTriangulation(List<Point2D> points, List<Triangle> triangles) {
        int width = 800;
        int height = 600;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLUE);

        g.setStroke(new BasicStroke(1.5f));
        System.out.println("List Triangles: " + triangles);
        for (Triangle triangle : triangles) {
            drawTriangle(g, triangle);
            System.out.println("Drawing triangle: " + triangle.p1 + ", " + triangle.p2 + ", " + triangle.p3);
        }
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            int x = (int) point.getX();
            int y = (int) point.getY();
            g.fillOval(x - 2, y - 2, 4, 4);
            g.drawString(Integer.toString(i), x + 5, y - 5); // Draw number above the point
        }



        g.dispose();

        return image;
    }

    static void drawTriangle(Graphics2D g, Triangle triangle) {
        System.out.println("Drawing triangle: " + triangle.p1 + ", " + triangle.p2 + ", " + triangle.p3);
        Path2D path = new Path2D.Double();
        path.moveTo(triangle.p1.getX(), triangle.p1.getY());
        path.lineTo(triangle.p2.getX(), triangle.p2.getY());
        path.lineTo(triangle.p3.getX(), triangle.p3.getY());
        path.closePath();
        g.draw(path);

    }


    public static void main(String[] args) {
        // Создаем список точек
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(100, 100));
        points.add(new Point2D(100, 200));
        points.add(new Point2D(200, 100));
        points.add(new Point2D(200, 200));

        points.add(new Point2D(250, 150));
        points.add(new Point2D(244.895, 180.901));
        points.add(new Point2D(230.691, 209.396));
        points.add(new Point2D(209.396, 230.691));
        points.add(new Point2D(180.901, 244.895));
        points.add(new Point2D(150, 250));
        points.add(new Point2D(119.099, 244.895));
        points.add(new Point2D(90.604, 230.691));
        points.add(new Point2D(69.309, 209.396));
        points.add(new Point2D(50, 180.901));
        points.add(new Point2D(44.895, 150));
        points.add(new Point2D(50, 119.099));
        points.add(new Point2D(69.309, 90.604));
        points.add(new Point2D(90.604, 69.309));
        points.add(new Point2D(119.099, 50));
        points.add(new Point2D(150, 44.895));
        points.add(new Point2D(180.901, 50));
        points.add(new Point2D(209.396, 69.309));
        points.add(new Point2D(230.691, 90.604));
        points.add(new Point2D(244.895, 119.099));
        points.add(new Point2D(250, 150));
        points.add(new Point2D(244.895, 180.901));
        points.add(new Point2D(230.691, 209.396));
        points.add(new Point2D(209.396, 230.691));
        points.add(new Point2D(180.901, 244.895));
        // Выполняем триангуляцию
        List<Triangle> triangles = DelaunayTriangulation.delaunayTriangulation(points);

        // Рисуем триангуляцию и точки
        BufferedImage image = DelaunayTriangulation.drawDelaunayTriangulation(points, triangles);


        // Сохраняем изображение в файл
        try {
            File outputImage = new File("delaunay_triangulation.png");
            ImageIO.write(image, "png", outputImage);
            System.out.println("Изображение сохранено в файл: " + outputImage.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении изображения: " + e.getMessage());
        }
    }
}
