package customskinloader.fake;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import customskinloader.CustomSkinLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class FakeClientPlayer {
    //For Legacy Skin
    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        //CustomSkinLoader.logger.debug("FakeClientPlayer/getDownloadImageSkin "+username);
        TextureManager textman = Minecraft.getMinecraft().getTextureManager();
        ITextureObject ito = textman.getTexture(resourceLocationIn);

        if (ito == null || !(ito instanceof ThreadDownloadImageData)) {
            //if Legacy Skin for username not loaded yet
            SkinManager skinman = Minecraft.getMinecraft().getSkinManager();
            UUID offlineUUID = EntityPlayer.getOfflineUUID(username);
            GameProfile offlineProfile = new GameProfile(offlineUUID, username);

            //Load Default Skin
            ResourceLocation defaultSkin = DefaultPlayerSkin.getDefaultSkin(offlineUUID);
            ITextureObject defaultSkinObj = new SimpleTexture(defaultSkin);
            textman.loadTexture(resourceLocationIn, defaultSkinObj);

            //Load Skin from SkinManager
            skinman.loadProfileTextures(offlineProfile, new LegacyBuffer(resourceLocationIn), false);
        }

        if (ito instanceof ThreadDownloadImageData)
            return (ThreadDownloadImageData) ito;
        else
            return null;
    }

    public static ResourceLocation getLocationSkin(String username) {
        //CustomSkinLoader.logger.debug("FakeClientPlayer/getLocationSkin "+username);
        return new ResourceLocation("skins/legacy-" + StringUtils.stripControlCodes(username));
    }

    private static class LegacyBuffer implements SkinAvailableCallback {
        ResourceLocation resourceLocationIn;
        boolean loaded = false;

        public LegacyBuffer(ResourceLocation resourceLocationIn) {
            CustomSkinLoader.logger.debug("Loading Legacy Texture (" + resourceLocationIn + ")");
            this.resourceLocationIn = resourceLocationIn;
        }

        @Override
        public void skinAvailable(Type typeIn, ResourceLocation location, MinecraftProfileTexture profileTexture) {
            if (typeIn != Type.SKIN || loaded)
                return;

            loaded = true;


            TextureManager textman = Minecraft.getMinecraft().getTextureManager();
            ITextureObject ito = textman.getTexture(location);

            if (ito != null)
                textman.loadTexture(resourceLocationIn, ito);
            CustomSkinLoader.logger.debug("Legacy Texture (" + resourceLocationIn + ") Loaded as " +
                    (ito == null ? null : ito.toString()) + " (" + location + ")");
            //textman.bindTexture(resourceLocationIn);
        }

    }
}
