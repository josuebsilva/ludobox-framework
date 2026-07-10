package core.com.gameframework.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class AssetLoader implements Disposable{
    private final AssetManager manager;

    public AssetLoader() {
        manager = new AssetManager(new InternalFileHandleResolver());
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

    public void queueTexture(String path) { manager.load(path, Texture.class);}
    public void queueAtlas(String path) { manager.load(path, TextureAtlas.class);}
    public void queueSound(String path) { manager.load(path, Sound.class);}
    public void queueMusic(String path) { manager.load(path, Music.class);}
    public void queueFont(String path) { manager.load(path, BitmapFont.class);}
    public void queueTiledMap(String path) { manager.load(path, TiledMap.class);}

    public boolean update() { return manager.update();}
    public void finishLoading() {manager.finishLoading();}
    public float getProgress() {return manager.getProgress();}

    public Texture getTexture(String path) { return manager.get(path, Texture.class);}
    public TextureAtlas getTextureAtlas(String path) { return manager.get(path, TextureAtlas.class);}
    public Sound getSound(String path) { return manager.get(path, Sound.class);}
    public Music getMusic(String path) { return manager.get(path, Music.class);}
    public BitmapFont getFont(String path) { return manager.get(path, BitmapFont.class);}
    public TiledMap getTiledMap(String path) { return manager.get(path, TiledMap.class);}

    public boolean isLoaded(String path) { return manager.isLoaded(path);}
    public void unload(String path) { manager.unload(path);}

    public <T> T getFile(Class<T> typeFile, String path) {
        try {
            return typeFile.getDeclaredConstructor(String.class).newInstance(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TextureAtlas loadAtlas(String path) {
        if (!manager.isLoaded(path)) {
            manager.load(path, TextureAtlas.class);
            manager.finishLoadingAsset(path);
        }
        return manager.get(path, TextureAtlas.class);
    }

    public Texture loadTexture(String path) {
        if (!manager.isLoaded(path)) {
            manager.load(path, Texture.class);
            manager.finishLoadingAsset(path);
        }
        return manager.get(path, Texture.class);
    }

    public Music loadMusic(String path) {
        if (!manager.isLoaded(path)) {
            manager.load(path, Music.class);
            manager.finishLoadingAsset(path);
        }
        return manager.get(path, Music.class);
    }

    public Sound loadSound(String path) {
        if (!manager.isLoaded(path)) {
            manager.load(path, Sound.class);
            manager.finishLoadingAsset(path);
        }
        return manager.get(path, Sound.class);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
