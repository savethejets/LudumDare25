package com.ludumdare.controller;

import com.ludumdare.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class EntitySpawner {
    private static EntitySpawner ourInstance = new EntitySpawner();

    private List<AbstractEntity> objectsToSpawn = new ArrayList<AbstractEntity>();

    public static EntitySpawner getInstance() {
        return ourInstance;
    }

    private EntitySpawner() {
    }

    public void spawn(AbstractEntity abstractEntity) {
        objectsToSpawn.add(abstractEntity);
    }

    public void collectObjectsToSpawn(List<AbstractEntity> gameObjectList) {
        gameObjectList.addAll(objectsToSpawn);
        objectsToSpawn.clear();
    }
}
