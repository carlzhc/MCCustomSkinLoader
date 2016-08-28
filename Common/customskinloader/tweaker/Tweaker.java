package customskinloader.tweaker;

import customskinloader.Logger;
import customskinloader.utils.MinecraftUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class Tweaker implements ITweaker {

	private String[] args;
	
	public static Logger logger = new Logger();

	public void acceptOptions(List args, File gameDir, File assetsDir, String profile) {
		MinecraftUtil.minecraftDataFolder=gameDir;
		File tweakerLogFile = new File(MinecraftUtil.getMinecraftDataDir(),"CustomSkinLoader/Tweaker.log");
		logger = new Logger(tweakerLogFile);
		
		logger.info("Using Tweaker");
		logger.info("Tweaker: acceptOptions");
		String[] temp={"--gameDir",gameDir.getAbsolutePath(),"--assetsDir",assetsDir.getAbsolutePath(),"--version",profile};
		this.args=ArrayUtils.addAll((String[])args.toArray(), temp);
	}

	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		logger.info("Tweaker: injectIntoClassLoader");
		logger.info("Loaded as a library.");
	}

	public String getLaunchTarget() {
		logger.info("Tweaker: getLaunchTarget");
		return "net.minecraft.client.main.Main";
	}

	public String[] getLaunchArguments() {
		logger.info("Tweaker: getLaunchArguments");
		return args;
	}
}