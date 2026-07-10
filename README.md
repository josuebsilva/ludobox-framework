# 🚀 Ludobox Framework

This is a simple 2D Game Framework.

![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/license-MIT-green)

## ✨ Features

- ✅ Game Objects and Components
- ✅ Assets Manager
- ✅ Scene Manager
- ✅ 2D Render
---

## 🛠 Technologies

- Java 21
- LibGDX
- OpenGL

---

## 📁 Folder Structure

```text
project/
├── assets/
│   ├── textures/
│   └── sounds/
├── src/
│   ├── core/
│   │   ├── assets/
│   │   ├── components/
│   │   └── gameobjects/
        └── scenes/
        └── ui/
        └── core/
│   └── game/
├── docs/
└── README.md
```

---

## ⚙️ Install

Clone repository:

```bash
git clone https://github.com/josuebsilva/ludobox-java.git
cd project
```

Execute:

```bash
./run
```

---

## 🎮 How use

Example Game Object:

```java
GameObject player = instantiate("Player");

player.addComponent(
    new SpriteRenderer(
        assetLoader.getTexture("player.png")
    )
);

scene.add(player);
```

---

## 💡 Code Example

### Asset Manager

```java
AssetLoader assets = new AssetLoader();

assets.queueTexture("background.png");
assets.loadAll();

Texture background =
    assets.getTexture("background.png");
```

### Components System

```java
public class PlayerController extends Component {

    @Override
    public void onUpdate(float deltaTime) {
        if (Gdx.input.isKeyPressed(Keys.A)) {
            transform.position.x -= speed * delta;
        }
    }
}
```

---

## 🗺 Roadmap

- [x] GameObject Systems
- [x] Sprite renders
- [x] Components System
- [ ] 2D Physics
- [ ] UI
- [ ] Particle System
- [ ] Multplatform