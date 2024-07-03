package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Point2D {
    double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;

    }
}

class Point implements Comparable<Point>{
    double x, y, z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {return "(" + x + ", " + y + ", " + z + ")";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int compareTo(Point other) {
        if (x != other.x) return Double.compare(x, other.x);
        if (y != other.y) return Double.compare(y, other.y);
        return Double.compare(z, other.z);
    }
}


public class Main {

    public static List<List<Point>> getAllUniquePairs(List<Point> points) {
        List<List<Point>> uniquePairs = new ArrayList<>();
        for (Point p1 : points) {
            for (Point p2 : points) {
                List<Point> reverse = new ArrayList<>();
                reverse.add(p2);
                reverse.add(p1);
                if (!p1.equals(p2) && !uniquePairs.contains(reverse)) {
                    List<Point> pair = new ArrayList<>();
                    pair.add(p1);
                    pair.add(p2);
                    uniquePairs.add(pair);
                }
            }
        }

        return uniquePairs;
    }

    public static Point findIntersection(Point P1, Point P2, Point P3, Point P4) {
        double determinant = (P3.x - P1.x) * (P2.y - P1.y) * (P4.z - P3.z)
                + (P2.x - P1.x) * (P4.y - P3.y) * (P3.z - P1.z)
                + (P3.y - P1.y) * (P2.z - P1.z) * (P4.x - P3.x)
                - (P3.z - P1.z) * (P2.y - P1.y) * (P4.z - P3.x)
                - (P4.y - P3.y) * (P3.x - P1.x) * (P2.z - P1.z)
                - (P3.y - P1.y) * (P4.z - P3.z) * (P2.x - P1.x);

        double m1 = (P2.x - P1.x);
        double m2 = (P4.x - P3.x);

        double n1 = (P2.y - P1.y);
        double n2 = (P4.y - P3.y);

        double r1 = (P2.z - P1.z);
        double r2 = (P4.z - P3.z);

        double x = 0;
        double y = 0;
        double z = 0;

        if ((m1 == 0 && m2 == 0)) {
            x = 2;
        }
        if ((n1 == 0 && n2 == 0)) {
            y = 2;
        }
        if ((r1 == 0 && r2 == 0)) {
            z = 2;
        }
        if ((m1 == 0 && m2 != 0) || (m2 == 0 && m1 != 0)) {
            x = 1;
        }
        if ((n1 == 0 && n2 != 0) || (n2 == 0 && n1 != 0)) {
            y = 1;
        }
        if ((r1 == 0 && r2 != 0) || (r2 == 0 && r1 != 0)) {
            z = 1;
        }

        if (x + y + z == 2 && (x == 2 || y == 2 || z == 2)) {
            Point2D Po1 = null;
            Point2D Po2 = null;
            Point2D Po3 = null;
            Point2D Po4 = null;
            if (z == 2) {
                Po1 = new Point2D(P1.x, P1.y);
                Po2 = new Point2D(P2.x, P2.y);
                Po3 = new Point2D(P3.x, P3.y);
                Po4 = new Point2D(P4.x, P4.y);
            }
            if (y == 2) {
                Po1 = new Point2D(P1.x, P1.z);
                Po2 = new Point2D(P2.x, P2.z);
                Po3 = new Point2D(P3.x, P3.z);
                Po4 = new Point2D(P4.x, P4.z);
            }
            if (x == 2) {
                Po1 = new Point2D(P1.y, P1.z);
                Po2 = new Point2D(P2.y, P2.z);
                Po3 = new Point2D(P3.y, P3.z);
                Po4 = new Point2D(P4.y, P4.z);
            }

            double Y = (-Po3.y * (Po4.x - Po3.x) * (Po2.y - Po1.y) + Po3.x * (Po4.y - Po3.y) * (Po2.y - Po1.y) - Po1.x * (Po2.y - Po1.y) + Po1.y * (Po2.x - Po1.x) * (Po4.y - Po3.y)) / ((Po2.x - Po1.x) * (Po4.y - Po3.y));
            double X = (-Po1.y * (Po2.x - Po1.x) + Po1.x * (Po2.y - Po1.y) + Y * (Po2.x - Po1.x)) / (Po2.y - Po1.y);

            if (z == 2) {
                return new Point(X, Y, P4.z);
            }
            if (y == 2) {
                return new Point(X, P1.y, Y);
            }
            if (x == 2) {
                return new Point(P1.x, X, Y);
            }
        } else {

            if (determinant == 0.0 && m1 != 0 && m2 != 0 && n1 != 0 && n2 != 0 && r1 != 0 && r2 != 0) {

                double S = ((P3.y - P1.y) * (P2.x - P1.x) - P3.x + P1.x) / ((P2.y - P1.y) * (P4.x - P3.x) - (P4.y - P3.y) * (P2.x - P1.x));

                return new Point((S * (P4.x - P3.x) + P3.x), (S * (P4.y - P3.y) + P3.y), (S * (P4.z - P3.z) + P3.z));
            } else {
                return null;
            }
        }
        return null;
    }

    public static Point2D findIntersection2D(Point2D P1, Point2D P2, Point2D P3, Point2D P4) {

        double X = ((P1.x * P2.y - P1.y * P2.x) * (P3.x - P4.x) - (P1.x - P2.x) * (P3.x * P4.y - P3.y * P4.x)) / ((P1.x - P2.x) * (P3.y - P4.y) - (P1.y - P2.y) * (P3.x - P4.x));
        double Y = ((P1.x * P2.y - P1.y * P2.x) * (P3.y - P4.y) - (P1.y - P2.y) * (P3.x * P4.y - P3.y * P4.x)) / ((P1.x - P2.x) * (P3.y - P4.y) - (P1.y - P2.y) * (P3.x - P4.x));
        double num = (X-P1.x)*(P2.y - P1.y) - (Y-P1.y)*(P2.x - P1.x);
        if (Double.isFinite(Y) && Double.isFinite(X) && !Double.isInfinite(X)
                && !Double.isInfinite(Y) && num == 0 && (((X < P1.x) && (X > P2.x)) || (X > P1.x) && (X < P2.x))) {
            return new Point2D(X, Y);
        }

        return null;
    }

    public static boolean contains(List<Point> points, Point point) {
        for (Point point_temp : points) {
            if (point_temp.x == point.x && point_temp.y == point.y && point_temp.z == point.z)
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 1, 1));
        points.add(new Point(1, 2, 2));
        points.add(new Point(1, 2, 1));
        points.add(new Point(1, 1, 2));
        points.add(new Point(2, 1, 1));
        points.add(new Point(2, 2, 1));
        points.add(new Point(2, 2, 2));
        points.add(new Point(2, 1, 2));

        List<List<Point>> uniquePairs = getAllUniquePairs(points);

        Point2D center = new Point2D((findMaxX(points) + findMinX(points)) / 2, (findMaxZ(points) + findMinZ(points)) / 2);
        System.out.println("len = " + uniquePairs.size());
        List<List<Point>> delite = new ArrayList<>();
        for (List<Point> otres1 : uniquePairs) {
            for (List<Point> otres2 : uniquePairs) {
                if (!otres2.equals(otres1) && ((findIntersection(otres1.get(0), otres1.get(1), otres2.get(0), otres2.get(1)) != null) && (!contains(points, findIntersection(otres1.get(0), otres1.get(1), otres2.get(0), otres2.get(1)))))) {
                    delite.add(otres1);
                    delite.add(otres2);
                }
            }
        }


        uniquePairs.removeAll(delite);

        System.out.println("len = " + uniquePairs.size());
        for (List<Point> pair : uniquePairs) {
            System.out.println("Pair: " + pair.get(0) + ", " + pair.get(1));
        }

        List<List<Point>> plane = new ArrayList<>();
        List<Point> intersec = new ArrayList<>();
        for (List<Point> pair : uniquePairs) {
            for (List<Point> nextPair : uniquePairs) {
                if (!pair.equals(nextPair) && (pair.get(0) == nextPair.get(0) || pair.get(1) == nextPair.get(1) || pair.get(0) == nextPair.get(1) || pair.get(1) == nextPair.get(0))) {
                    List<Point> triplet = new ArrayList<>();
                    intersec.clear();
                    if ((pair.get(0) == nextPair.get(0))) {
                        triplet.add(pair.get(0));
                        triplet.add(pair.get(1));
                        triplet.add(nextPair.get(1));
                        intersec.add(pair.get(1));
                        intersec.add(nextPair.get(1));
                    } else if ((pair.get(1) == nextPair.get(0))) {
                        triplet.add(pair.get(0));
                        triplet.add(pair.get(1));
                        triplet.add(nextPair.get(1));
                        intersec.add(pair.get(0));
                        intersec.add(nextPair.get(1));
                    } else if ((pair.get(0) == nextPair.get(1))) {
                        triplet.add(pair.get(0));
                        triplet.add(pair.get(1));
                        triplet.add(nextPair.get(0));
                        intersec.add(pair.get(1));
                        intersec.add(nextPair.get(0));
                    } else {
                        triplet.add(pair.get(0));
                        triplet.add(pair.get(1));
                        triplet.add(nextPair.get(0));
                        intersec.add(pair.get(0));
                        intersec.add(nextPair.get(0));
                    }

                    double X = ((triplet.get(1).y - triplet.get(0).y) * (triplet.get(2).z - triplet.get(0).z) - (triplet.get(2).y - triplet.get(0).y) * (triplet.get(1).z - triplet.get(0).z));
                    double Y = ((triplet.get(1).x - triplet.get(0).x) * (triplet.get(2).z - triplet.get(0).z) - (triplet.get(2).x - triplet.get(0).x) * (triplet.get(1).z - triplet.get(0).z));
                    double Z = ((triplet.get(1).x - triplet.get(0).x) * (triplet.get(2).y - triplet.get(0).y) - (triplet.get(2).x - triplet.get(0).x) * (triplet.get(1).y - triplet.get(0).y));
                    double C = (X * (-triplet.get(0).x)) + (Y * (-triplet.get(0).y)) + (Z * (-triplet.get(0).z));

                    for (Point point : points) {
                        if (!triplet.contains(point)) {
                            if (X * point.x + Y * point.y + Z * point.z + C == 0) {
                            triplet.add(point);
                            }
                        }
                    }

                    for (Point point : points) {
                        Point2D changed = new Point2D(point.x, point.z);
                        Point2D changed2 = new Point2D(intersec.get(0).x, intersec.get(0).z);
                        Point2D changed3 = new Point2D(intersec.get(1).x, intersec.get(1).z);
                        if (!triplet.contains(point)
                                && (findIntersection2D(center, changed, changed2, changed3) == null
                                || points.contains(findIntersection2D(center, changed, changed2, changed3)))
                                && !plane.contains(triplet)) {
                            plane.add(triplet);
                        }
                    }
                }
            }
        }

        List<List<Point>> result = removeDuplicatePlanes(plane);
        System.out.println("len_result = " + result.size());
        for (int i = 0; i < result.size(); i++) {
            System.out.println("i = " + i + " result: " + result.get(i).get(0) + ", " + result.get(i).get(1) + ", " + result.get(i).get(2) + ", "+ result.get(i).get(3));
        }
        List<DelaunayTriangulation.Point2D> poin= new ArrayList<>();
        List<DelaunayTriangulation.Triangle> trel= new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            List<DelaunayTriangulation.Triangle> triangles = DelaunayTriangulation.delaunayTriangulation(reconstraction(result.get(i)));

            for (int j = 0; j < reconstraction(result.get(i)).size(); j++){
                poin.add(reconstraction(result.get(i)).get(j));
            }
                trel.add(triangles.get(i));
        }
        BufferedImage image = DelaunayTriangulation.drawDelaunayTriangulation(poin, trel);
        try {
            File outputImage = new File("delaunay_triangulation2.png");
            ImageIO.write(image, "png", outputImage);
            System.out.println("Изображение сохранено в файл: " + outputImage.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении изображения: " + e.getMessage());
        }
    }


    public static List<DelaunayTriangulation.Point2D> reconstraction (List<Point> input) {
        List<DelaunayTriangulation.Point2D> output = new ArrayList<>();
        Point v1 = new Point(input.get(1).x -input.get(0).x,input.get(1).y -input.get(0).y , input.get(1).z - input.get(0).z);
        Point v2 = new Point(input.get(2).x -input.get(0).x,input.get(2).y -input.get(0).y , input.get(2).z - input.get(0).z);

        Point n = new Point((v1.y * v2.z)-(v1.z * v2.y), (v1.x * v2.z)-(v1.z * v2.x), (v1.x * v2.y)-(v1.y * v2.x));

        double v1_1 = Math.sqrt(Math.pow(v1.x, 2) + Math.pow(v1.y, 2) + Math.pow(v1.z, 2));
        Point u1 = new Point(v1.x/v1_1, v1.y/v1_1, v1.z/v1_1);

        Point t = new Point((n.y * u1.z)-(n.z * u1.y), (n.x * u1.z)-(n.z * u1.x), (n.x * u1.y)-(n.y * u1.x));

        double t_1 = Math.sqrt(Math.pow(t.x, 2) + Math.pow(t.y, 2) + Math.pow(t.z, 2));

        Point u2 = new Point(t.x/t_1, t.y/t_1, t.z/t_1);


        for (int i = 0; i < input.size(); i++){
            Point P = new Point(input.get(i).x - input.get(0).x,input.get(i).y - input.get(0).y, input.get(i).z - input.get(0).z);
            DelaunayTriangulation.Point2D D_2 = new DelaunayTriangulation.Point2D((u1.x * P.x) + (u1.y * P.y) + (u1.z * P.z), (u2.x * P.x) + (u2.y * P.y) + (u2.z * P.z));
            output.add(D_2);
        }


        return output;
    }

    public static List<List<Point>> removeDuplicatePlanes(List<List<Point>> input) {
        Set<List<Point>> set = new HashSet<>();
        for (List<Point> plane : input) {
            plane.sort(Point::compareTo); // Сортировка точек в плоскости
            set.add(plane);
        }
        return new ArrayList<>(set);
    }

    public static double findMaxX(List<Point> points) {
        double maxX = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.x > maxX) {
                maxX = point.x;
            }
        }
        return maxX;
    }

    public static double findMaxZ(List<Point> points) {
        double maxZ = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.z > maxZ) {
                maxZ = point.z;
            }
        }
        return maxZ;
    }

    public static double findMinX(List<Point> points) {
        double minX = Integer.MAX_VALUE;
        for (Point point : points) {
            if (point.x < minX) {
                minX = point.x;
            }
        }
        return minX;
    }

    public static double findMinZ(List<Point> points) {
        double minZ = Integer.MAX_VALUE;
        for (Point point : points) {
            if (point.z < minZ) {
                minZ = point.z;
            }
        }
        return minZ;
    }

}

