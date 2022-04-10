package me.mcblueparrot.mixintemplate.tweak;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class ClientTweaker implements ITweaker {

	private static List<String> args = new ArrayList<>();

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		ClientTweaker.args.addAll(args);
		if(gameDir != null) {
			ClientTweaker.args.add("--gameDir");
			ClientTweaker.args.add(gameDir.getAbsolutePath());
		}
		if(assetsDir != null) {
			ClientTweaker.args.add("--assetsDir");
			ClientTweaker.args.add(assetsDir.getAbsolutePath());
		}
		if(profile != null) {
			ClientTweaker.args.add("--version");
			ClientTweaker.args.add(profile);
		}
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.mixintemplate.json");

		MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();

		if(environment.getObfuscationContext() == null) {
			environment.setObfuscationContext("notch");
		}

		environment.setSide(Side.CLIENT);
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.main.Main";
	}

	@Override
	public String[] getLaunchArguments() {
		return args.toArray(new String[0]);
	}

}
