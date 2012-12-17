package com.ludumdare.entities;

import java.util.List;

public interface Collidable {
    public void testCollision(List<AbstractEntity> entities);
}
