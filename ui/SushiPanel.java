package typingapp.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SushiPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private static final int SUSHI_WIDTH = 50;
    private static final int SUSHI_HEIGHT = 50;

    private int sushiX = -SUSHI_WIDTH;
    private BufferedImage sushiImage;

    private final Random random = new Random();
    
    public SushiPanel(String difficulty) {
        loadSushiImage(difficulty);
    }

    private String getFolderName(String difficulty) {

        switch (difficulty) {

            case "初級":
                return "寿司緑皿";

            case "中級":
                return "寿司青皿";

            case "上級":
                return "寿司赤皿";

            default:
                throw new IllegalArgumentException("不明な難易度：" + difficulty);
        }
    }
    
    private void loadSushiImage(String difficulty) {

        try {

            String folder = getFolderName(difficulty);

            String fileName = chooseRandomImage(difficulty);

            String path =
                    "/images/sushi/"
                    + folder
                    + "/"
                    + fileName;

            System.out.println(path);

            var url = getClass().getResource(path);

            if (url == null) {
                throw new RuntimeException("画像がありません : " + path);
            }

            sushiImage = ImageIO.read(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int centerY = getHeight() / 2;

        g.setColor(Color.BLUE);

        g.drawLine(0,centerY,getWidth(),centerY);

        if (sushiImage != null) {
            g.drawImage(sushiImage,sushiX,centerY - SUSHI_HEIGHT / 2,SUSHI_WIDTH,SUSHI_HEIGHT,null);
        }
    }
    
    private List<String> loadSushiList(String difficulty) throws IOException {

        String listFile;

        switch (difficulty) {
            case "初級":
                listFile = "/sushi/寿司緑皿.txt";
                break;

            case "中級":
                listFile = "/sushi/寿司青皿.txt";
                break;

            case "上級":
                listFile = "/sushi/寿司赤皿.txt";
                break;

            default:
                throw new IllegalArgumentException("不明な難易度");
        }

        List<String> sushiList = new ArrayList<>();

        try (BufferedReader reader =
                new BufferedReader(
                    new InputStreamReader(
                        getClass().getResourceAsStream(listFile),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.isBlank()) {
                    sushiList.add(line.trim());
                }
            }
        }

        return sushiList;
    }
    
    private String chooseRandomImage(String difficulty) throws IOException {

        List<String> sushiList = loadSushiList(difficulty);

        if (sushiList.isEmpty()) {
            throw new RuntimeException("寿司リストが空です");
        }

        String sushiName = sushiList.get(random.nextInt(sushiList.size()));

        return sushiName + ".png";
    }
    
    public void changeSushi(String difficulty) {

        loadSushiImage(difficulty);

        resetPosition();

        repaint();
    }
    
    public void move() {

        int totalDistance = getWidth() + SUSHI_WIDTH;
        int step = Math.max(1,(int)Math.ceil(totalDistance / 100.0));

        sushiX += step;

        repaint();
    }

    public void resetPosition() {
        sushiX = -SUSHI_WIDTH;
        repaint();
    }

    public int getSushiPosition() {
    	return sushiX;
    }
}