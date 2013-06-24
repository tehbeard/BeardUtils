package me.tehbeard.utils.testSuite.fake;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class FakeWorld implements World{

    String name;
    public FakeWorld(String name){
        this.name= name;
    }
    
    
    public Set<String> getListeningPluginChannels() {
        // 
        return null;
    }

    public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
        // 
        
    }

    public List<MetadataValue> getMetadata(String arg0) {
        // 
        return null;
    }

    public boolean hasMetadata(String arg0) {
        // 
        return false;
    }

    public void removeMetadata(String arg0, Plugin arg1) {
        // 
        
    }

    public void setMetadata(String arg0, MetadataValue arg1) {
        // 
        
    }

    public boolean canGenerateStructures() {
        // 
        return false;
    }

    public boolean createExplosion(Location arg0, float arg1) {
        // 
        return false;
    }

    public boolean createExplosion(Location arg0, float arg1, boolean arg2) {
        // 
        return false;
    }

    public boolean createExplosion(double arg0, double arg1, double arg2,
            float arg3) {
        // 
        return false;
    }

    public boolean createExplosion(double arg0, double arg1, double arg2,
            float arg3, boolean arg4) {
        // 
        return false;
    }

    public Item dropItem(Location arg0, ItemStack arg1) {
        // 
        return null;
    }

    public Item dropItemNaturally(Location arg0, ItemStack arg1) {
        // 
        return null;
    }

    public boolean generateTree(Location arg0, TreeType arg1) {
        // 
        return false;
    }

    public boolean generateTree(Location arg0, TreeType arg1,
            BlockChangeDelegate arg2) {
        // 
        return false;
    }

    public boolean getAllowAnimals() {
        // 
        return false;
    }

    public boolean getAllowMonsters() {
        // 
        return false;
    }

    public int getAnimalSpawnLimit() {
        // 
        return 0;
    }

    public Biome getBiome(int arg0, int arg1) {
        // 
        return null;
    }

    public Block getBlockAt(Location arg0) {
        // 
        return null;
    }

    public Block getBlockAt(int arg0, int arg1, int arg2) {
        // 
        return null;
    }

    public int getBlockTypeIdAt(Location arg0) {
        // 
        return 0;
    }

    public int getBlockTypeIdAt(int arg0, int arg1, int arg2) {
        // 
        return 0;
    }

    public Chunk getChunkAt(Location arg0) {
        // 
        return null;
    }

    public Chunk getChunkAt(Block arg0) {
        // 
        return null;
    }

    public Chunk getChunkAt(int arg0, int arg1) {
        // 
        return null;
    }

    public Difficulty getDifficulty() {
        // 
        return null;
    }

    public ChunkSnapshot getEmptyChunkSnapshot(int arg0, int arg1,
            boolean arg2, boolean arg3) {
        // 
        return null;
    }

    public List<Entity> getEntities() {
        // 
        return null;
    }

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... arg0) {
        // 
        return null;
    }

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> arg0) {
        // 
        return null;
    }

    public Collection<Entity> getEntitiesByClasses(Class<?>... arg0) {
        // 
        return null;
    }

    public Environment getEnvironment() {
        // 
        return null;
    }

    public long getFullTime() {
        // 
        return 0;
    }

    public ChunkGenerator getGenerator() {
        // 
        return null;
    }

    public Block getHighestBlockAt(Location arg0) {
        // 
        return null;
    }

    public Block getHighestBlockAt(int arg0, int arg1) {
        // 
        return null;
    }

    public int getHighestBlockYAt(Location arg0) {
        // 
        return 0;
    }

    public int getHighestBlockYAt(int arg0, int arg1) {
        // 
        return 0;
    }

    public double getHumidity(int arg0, int arg1) {
        // 
        return 0;
    }

    public boolean getKeepSpawnInMemory() {
        // 
        return false;
    }

    public List<LivingEntity> getLivingEntities() {
        // 
        return null;
    }

    public Chunk[] getLoadedChunks() {
        // 
        return null;
    }

    public int getMaxHeight() {
        // 
        return 0;
    }

    public int getMonsterSpawnLimit() {
        // 
        return 0;
    }

    public String getName() {
        // 
        return name;
    }

    public boolean getPVP() {
        // 
        return false;
    }

    public List<Player> getPlayers() {
        // 
        return null;
    }

    public List<BlockPopulator> getPopulators() {
        // 
        return null;
    }

    public int getSeaLevel() {
        // 
        return 0;
    }

    public long getSeed() {
        // 
        return 0;
    }

    public Location getSpawnLocation() {
        // 
        return null;
    }

    public double getTemperature(int arg0, int arg1) {
        // 
        return 0;
    }

    public int getThunderDuration() {
        // 
        return 0;
    }

    public long getTicksPerAnimalSpawns() {
        // 
        return 0;
    }

    public long getTicksPerMonsterSpawns() {
        // 
        return 0;
    }

    public long getTime() {
        // 
        return 0;
    }

    public UUID getUID() {
        // 
        return null;
    }

    public int getWaterAnimalSpawnLimit() {
        // 
        return 0;
    }

    public int getWeatherDuration() {
        // 
        return 0;
    }

    public File getWorldFolder() {
        // 
        return null;
    }

    public WorldType getWorldType() {
        // 
        return null;
    }

    public boolean hasStorm() {
        // 
        return false;
    }

    public boolean isAutoSave() {
        // 
        return false;
    }

    public boolean isChunkLoaded(Chunk arg0) {
        // 
        return false;
    }

    public boolean isChunkLoaded(int arg0, int arg1) {
        // 
        return false;
    }

    public boolean isThundering() {
        // 
        return false;
    }

    public void loadChunk(Chunk arg0) {
        // 
        
    }

    public void loadChunk(int arg0, int arg1) {
        // 
        
    }

    public boolean loadChunk(int arg0, int arg1, boolean arg2) {
        // 
        return false;
    }

    public void playEffect(Location arg0, Effect arg1, int arg2) {
        // 
        
    }

    public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
        // 
        
    }

    public void playEffect(Location arg0, Effect arg1, int arg2, int arg3) {
        // 
        
    }

    public <T> void playEffect(Location arg0, Effect arg1, T arg2, int arg3) {
        // 
        
    }

    public boolean refreshChunk(int arg0, int arg1) {
        // 
        return false;
    }

    public boolean regenerateChunk(int arg0, int arg1) {
        // 
        return false;
    }

    public void save() {
        // 
        
    }

    public void setAnimalSpawnLimit(int arg0) {
        // 
        
    }

    public void setAutoSave(boolean arg0) {
        // 
        
    }

    public void setBiome(int arg0, int arg1, Biome arg2) {
        // 
        
    }

    public void setDifficulty(Difficulty arg0) {
        // 
        
    }

    public void setFullTime(long arg0) {
        // 
        
    }

    public void setKeepSpawnInMemory(boolean arg0) {
        // 
        
    }

    public void setMonsterSpawnLimit(int arg0) {
        // 
        
    }

    public void setPVP(boolean arg0) {
        // 
        
    }

    public void setSpawnFlags(boolean arg0, boolean arg1) {
        // 
        
    }

    public boolean setSpawnLocation(int arg0, int arg1, int arg2) {
        // 
        return false;
    }

    public void setStorm(boolean arg0) {
        // 
        
    }

    public void setThunderDuration(int arg0) {
        // 
        
    }

    public void setThundering(boolean arg0) {
        // 
        
    }

    public void setTicksPerAnimalSpawns(int arg0) {
        // 
        
    }

    public void setTicksPerMonsterSpawns(int arg0) {
        // 
        
    }

    public void setTime(long arg0) {
        // 
        
    }

    public void setWaterAnimalSpawnLimit(int arg0) {
        // 
        
    }

    public void setWeatherDuration(int arg0) {
        // 
        
    }

    public <T extends Entity> T spawn(Location arg0, Class<T> arg1)
            throws IllegalArgumentException {
        // 
        return null;
    }

    public Arrow spawnArrow(Location arg0, Vector arg1, float arg2, float arg3) {
        // 
        return null;
    }

    public LivingEntity spawnCreature(Location arg0, EntityType arg1) {
        // 
        return null;
    }

    public LivingEntity spawnCreature(Location arg0, CreatureType arg1) {
        // 
        return null;
    }

    public LightningStrike strikeLightning(Location arg0) {
        // 
        return null;
    }

    public LightningStrike strikeLightningEffect(Location arg0) {
        // 
        return null;
    }

    public boolean unloadChunk(Chunk arg0) {
        // 
        return false;
    }

    public boolean unloadChunk(int arg0, int arg1) {
        // 
        return false;
    }

    public boolean unloadChunk(int arg0, int arg1, boolean arg2) {
        // 
        return false;
    }

    public boolean unloadChunk(int arg0, int arg1, boolean arg2, boolean arg3) {
        // 
        return false;
    }

    public boolean unloadChunkRequest(int arg0, int arg1) {
        // 
        return false;
    }

    public boolean unloadChunkRequest(int arg0, int arg1, boolean arg2) {
        // 
        return false;
    }


    public Entity spawnEntity(Location arg0, EntityType arg1) {
        // 
        return null;
    }


    public boolean isChunkInUse(int arg0, int arg1) {
        // 
        return false;
    }


    public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
        // 
        
    }


    public FallingBlock spawnFallingBlock(Location arg0, Material arg1,
            byte arg2) throws IllegalArgumentException {
        // 
        return null;
    }


    public FallingBlock spawnFallingBlock(Location arg0, int arg1, byte arg2)
            throws IllegalArgumentException {
        // 
        return null;
    }


    public boolean createExplosion(double arg0, double arg1, double arg2,
            float arg3, boolean arg4, boolean arg5) {
        // 
        return false;
    }


    public int getAmbientSpawnLimit() {
        // 
        return 0;
    }


    public String getGameRuleValue(String arg0) {
        // 
        return null;
    }


    public String[] getGameRules() {
        // 
        return null;
    }


    public boolean isGameRule(String arg0) {
        // 
        return false;
    }


    public void setAmbientSpawnLimit(int arg0) {
        // 
        
    }


    public boolean setGameRuleValue(String arg0, String arg1) {
        // 
        return false;
    }

}
