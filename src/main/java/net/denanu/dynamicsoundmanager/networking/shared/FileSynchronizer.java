package net.denanu.dynamicsoundmanager.networking.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class FileSynchronizer {
	public static int outboundUUID = 0;
	public static int inboundUUID  = 0;

	public static HashMap<Integer, FileInputStream> outbounds = new HashMap<>();
	public static HashMap<Integer, FileOutputStream> inbounds = new HashMap<>();

	public synchronized static int openOutbound(final File file) throws FileNotFoundException {
		FileSynchronizer.outbounds.put(FileSynchronizer.outboundUUID, new FileInputStream(file));
		return FileSynchronizer.outboundUUID++;
	}

	public synchronized static void closeOutbound(final int uuid) {
		try {
			FileSynchronizer.outbounds.get(uuid).close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		FileSynchronizer.outbounds.remove(uuid);
	}

	public synchronized static int openInbound(final File file) throws FileNotFoundException {
		file.getParentFile().mkdirs();
		FileSynchronizer.inbounds.put(FileSynchronizer.inboundUUID, new FileOutputStream(file));
		return FileSynchronizer.inboundUUID++;
	}

	public synchronized static void closeInbound(final int uuid) {
		try {
			FileSynchronizer.inbounds.get(uuid).close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		FileSynchronizer.inbounds.remove(uuid);
	}

}
