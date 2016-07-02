package com.game.engine.graphics;


import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Getter
@Setter
public class FontTexture {

    private static final String IMAGE_FORMAT = "png";
    private final Font font;
    private Color color = Color.WHITE;
    private final String charSetName;
    private final Map<Character, CharInfo> charMap = new HashMap<>();
    private Texture texture;
    private int height;
    private int width;

    public FontTexture(Font font, String charSetName) throws Exception {
        this.font = font;
        this.charSetName = charSetName;
        buildTexture();
    }

    public FontTexture(Font font, Color color, String charSetName) throws Exception {
        this.font = font;
        this.charSetName = charSetName;
        this.color = color;
        buildTexture();
    }


    public CharInfo getCharInfo(char c) {
        return charMap.get(c);
    }

    private String getAllAvailableChars(String charsetName) {
        CharsetEncoder encoder = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char ch = 0; ch < Character.MAX_VALUE; ch++) {
            if (encoder.canEncode(ch)) {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private void buildTexture() throws Exception {
        // Get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charSetName);
        this.width = 0;
        this.height = 0;
        for (char c : allChars.toCharArray()) {
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }
        g2D.dispose();

        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(color);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();

        // Dump image to a byte buffer
        InputStream textureInputStream;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, IMAGE_FORMAT, out);
            out.flush();
            textureInputStream = new ByteArrayInputStream(out.toByteArray());
        }

        texture = new Texture(textureInputStream);
    }

    @Getter
    public static class CharInfo {

        private final int startX;
        private final int width;

        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }
    }
}