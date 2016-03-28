package Toolset;

import Graphs.Edge;
import Graphs.Graph;
import Graphs.NetworkGraph.RCEdge;
import Graphs.Vertex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Andrei C
 *
 *         code is a modified version of source code for Assignment 5, Winter 2014 Comp 202
 *         original author: Jonathan Trembley, University of McGill, COMP 202
 */

public class View extends JFrame {
    private Graph G;
    private List<Vertex> nodes;
    private ArrayList<Integer[]> v_pos;
    private ArrayList<Integer[]> pixel_v_pos;

    private int PAD = 0;
    private int MAX_X, MAX_Y;
    private int scale = 1024;
    private int pointSize = 15;

    final private Color red = Color.red;
    final private Color black = Color.BLACK;

    public View(Graph G, int counter) {
        super("Reliability Graph " + counter);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Map map = new Map();
        add(map);
        setSize(scale, scale);
        setVisible(true);
        v_pos = new ArrayList<Integer[]>();
        pixel_v_pos = new ArrayList<Integer[]>();
        this.G = G;
        this.nodes = Arrays.asList(G.getV());
        for (int i = 0; i < this.nodes.size(); i++) {
            int posX, posY;
            posX = (scale / 2) + (int) ((scale * 0.4) * Math.sin(i * (2 * 3.141592653) / this.nodes.size()));
            posY = (scale / 2) + (int) ((scale * 0.4) * Math.cos(i * (2 * 3.141592653) / this.nodes.size()));
            v_pos.add(new Integer[]{posX, posY});
        }
        this.MAX_X = this.MAX_Y = scale;
        this.repaint();
    }

    public class Map extends JPanel {
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.black);
            g.setFont(new Font("default", Font.PLAIN, 14));
            int w = getWidth();
            int h = getHeight();
            double scale_x = (double) (w - 2 * PAD) / MAX_X;
            double scale_y = (double) (h - 2 * PAD) / MAX_Y;

            // draw vertices

            for (int i = 0; i < nodes.size(); i++) {
                Vertex node = nodes.get(i);
                int posX = PAD + (int) (v_pos.get(i)[0] * scale_x);
                int posY = h - PAD - (int) (v_pos.get(i)[1] * scale_y);
                pixel_v_pos.add(new Integer[]{posX, posY});
                g2.setColor(red);
                g2.fill(new Ellipse2D.Double(posX - pointSize / 2, posY - pointSize / 2, pointSize, pointSize));
                g2.setColor(black);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
                g.drawString("" + node, posX - 8, posY+2);
            }

            // draw edges
            int index = 0;
            for (Edge e : G.getE()) {
                int adj = G.getAdjacencyMatrix()[index];
                if (e != null && adj != 0) {
                    g2.setColor(new Color(30, 30, 30, 100));
                    Vertex v1, v2;
                    v1 = e.getV1();
                    v2 = e.getV2();
                    int posX = pixel_v_pos.get(v1.getTag())[0];
                    int posY = pixel_v_pos.get(v1.getTag())[1];
                    int vPosX = pixel_v_pos.get(v2.getTag())[0];
                    int vPosY = pixel_v_pos.get(v2.getTag())[1];
                    g2.drawLine(posX, posY, vPosX, vPosY);
                    int angle = 90 - (int) Math.toDegrees(Math.atan2((posX - vPosX), (posY - vPosY)));
                    int distance = (int) (Math.sqrt(Math.pow(posX - vPosX, 2) + Math.pow(posY - vPosY, 2)));
                    if (angle >= 90 && angle <= 270)
                        angle -= 180;
                    int rX, rY;
                    if (angle > 180) {
                        rX = (int) vPosX;
                        rY = (int) vPosY;
                    } else {
                        rX = (int) posX;
                        rY = (int) posY;
                    }

                    g2.setColor(red);
                    int font = (int) (0.7 * Math.sqrt(distance));
                    if (font < 7) font = 7;
                    if (font > 11) font = 11;
                    g.setFont(new Font("TimesRoman", Font.PLAIN, font));
                    if (G.getN() < 20)
                    drawRotate(g2, rX, rY, angle, "[" + adj + "] " + ((RCEdge) G.getE()[index]));
                }
                index++;
            }
        }

        public void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) {
            g2d.translate((float) x, (float) y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text, 15 , 0);
            g2d.rotate(-Math.toRadians(angle));
            g2d.translate(-(float) x, -(float) y);
        }
    }


}
