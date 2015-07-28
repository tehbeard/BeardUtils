package com.tehbeard.utils.testSuite.syringe;

public class TestYaml {

    /*
     * @Inject List<ItemStack> l;
     * 
     * YamlConfiguration yc;
     * 
     * @Test public void testYamlItemStackList() throws FileNotFoundException,
     * IOException, InvalidConfigurationException{ yc = new YamlConfiguration();
     * l = new ArrayList<ItemStack>(); l.add(new ItemStack(1,2)); l.add(new
     * ItemStack(2,2)); l.add(null); l.add(new ItemStack(3,2));
     * 
     * for(ItemStack i : l){ System.out.println(i); }
     * 
     * new Injector<Object,Inject>(Inject.class){
     * 
     * @Override protected void doInject(Inject annotation, Object object, Field
     * field) throws IllegalArgumentException, IllegalAccessException {
     * yc.set("list",field.get(object));
     * 
     * }
     * 
     * }.inject(this); l = null;
     * 
     * 
     * String s = yc.saveToString(); System.out.println(s);
     * yc.loadFromString(s); Injector ii = new
     * Injector<Object,Inject>(Inject.class){
     * 
     * @Override protected void doInject(Inject annotation, Object object, Field
     * field) throws IllegalArgumentException, IllegalAccessException {
     * field.set(object,yc.getList("list"));
     * 
     * }
     * 
     * }; ii.inject(this);
     * 
     * for(ItemStack i : l){ System.out.println(i); }
     * 
     * }
     * 
     * 
     * @Test public void testLoadConfig(){ String s = "list:" + "\n" +
     * "  - foo: bar" + "\n" + "    bar: foo" + "\n" +
     * "  - ==: org.bukkit.inventory.ItemStack" + "\n" + "    type: STONE" +
     * "\n" + "    amount: 2";
     * 
     * YamlConfiguration y = new YamlConfiguration(); try { y.loadFromString(s);
     * for(Object o : y.getList("list")){ System.out.println("List of: " +
     * o.getClass().getSimpleName()); } } catch (InvalidConfigurationException
     * e) { e.printStackTrace(); } }
     */
}
