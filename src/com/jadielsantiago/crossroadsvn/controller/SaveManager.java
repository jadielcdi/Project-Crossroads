package com.jadielsantiago.crossroadsvn.controller;

import com.jadielsantiago.crossroadsvn.model.SaveState;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    private static final String SAVES_DIR_NAME = "saves";
    private static final int THUMB_WIDTH = 240;
    private static final int THUMB_HEIGHT = 135;

    public static File getSavesDirectory() {
        File dir = new File(SAVES_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private static File getSaveFile(int slotIndex) {
        return new File(getSavesDirectory(), "slot_" + slotIndex + ".dat");
    }

    private static File getThumbnailFile(int slotIndex) {
        return new File(getSavesDirectory(), "slot_" + slotIndex + ".png");
    }

    public static boolean hasSave(int slotIndex) {
        return getSaveFile(slotIndex).exists();
    }

    public static boolean saveGame(int slotIndex, SaveState state, WritableImage screenshot) {
        try {
            File saveFile = getSaveFile(slotIndex);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oos.writeObject(state);
            }

            // Save visual frame snapshot thumbnail if provided
            if (screenshot != null) {
                File thumbFile = getThumbnailFile(slotIndex);
                BufferedImage fxBuf = SwingFXUtils.fromFXImage(screenshot, null);
                if (fxBuf != null) {
                    BufferedImage scaled = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = scaled.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(fxBuf, 0, 0, THUMB_WIDTH, THUMB_HEIGHT, null);
                    g2d.dispose();

                    ImageIO.write(scaled, "png", thumbFile);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Failed to save game in slot " + slotIndex + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static SaveState loadGame(int slotIndex) {
        File saveFile = getSaveFile(slotIndex);
        if (!saveFile.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (SaveState) ois.readObject();
        } catch (Exception e) {
            System.err.println("Failed to load save from slot " + slotIndex + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteSave(int slotIndex) {
        boolean datDeleted = false;
        File saveFile = getSaveFile(slotIndex);
        if (saveFile.exists()) {
            datDeleted = saveFile.delete();
        }
        File thumbFile = getThumbnailFile(slotIndex);
        if (thumbFile.exists()) {
            thumbFile.delete();
        }
        return datDeleted;
    }

    public static Image getThumbnail(int slotIndex) {
        File thumbFile = getThumbnailFile(slotIndex);
        if (thumbFile.exists()) {
            try {
                return new Image(thumbFile.toURI().toString());
            } catch (Exception e) {
                System.err.println("Error loading thumbnail for slot " + slotIndex + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static Map<Integer, SaveState> loadSlotMetadataForPage(int page, int slotsPerPage) {
        Map<Integer, SaveState> slotMap = new HashMap<>();
        int startSlot = (page - 1) * slotsPerPage + 1;
        int endSlot = startSlot + slotsPerPage - 1;

        for (int i = startSlot; i <= endSlot; i++) {
            if (hasSave(i)) {
                SaveState state = loadGame(i);
                if (state != null) {
                    slotMap.put(i, state);
                }
            }
        }
        return slotMap;
    }
}
