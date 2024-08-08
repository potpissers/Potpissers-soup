package com.memeasaur.potpissersdefault.Util.Methods;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SerializationUtils {
    public static void writeBinary(String filename, Object object) {
        String tempFilename = filename + ".tmp";
        Path tempFilenamePath = Path.of(tempFilename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFilename))) {
            oos.writeObject(object);
            oos.close();
            Files.move(tempFilenamePath, Path.of(filename), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.deleteIfExists(tempFilenamePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Object readBinary(String filename, Object object) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object readObject = ois.readObject();
            if (readObject != null && readObject.getClass() == object.getClass())
                return readObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
    public static byte[] serializeBukkitByteArray(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
            boos.writeObject(object);
            return baos.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Object deserializeBukkit(byte[] byteArray, Object object) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            Object readObject = bois.readObject();
            if (readObject != null && readObject.getClass() == object.getClass())
                return readObject;
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
}
