// TODO : Revisit at later date, Refactor so each field is a filter object?
//package com.tehbeard.utils.commands;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class CommandArgumentParser {
//
//    private static final Pattern parse_expression = Pattern.compile("\\@([arp])(\\[(.*)\\])?");
//    private static final Pattern parse_arguments  = Pattern.compile("([a-zA-Z])\\=([0-9]*)");
//
//    @SuppressWarnings("deprecation")
//    public static List<Player> parseArgument(String argument, Location baseLocation) {
//        Location l = baseLocation.clone();
//        Matcher matcher = parse_expression.matcher(argument);
//        matcher.find();
//        String type = matcher.group(1);
//        Matcher am = parse_arguments.matcher(matcher.group(2));
//        Map<String, String> args = new HashMap<String, String>();
//
//        while (am.find()) {
//            args.put(am.group(1), am.group(2));
//        }
//
//        List<Player> players = baseLocation.getWorld().getPlayers();
//        // parse player filters
//        if (args.get("m") != null) {
//            int mode = Integer.parseInt(args.get("m"));
//            Iterator<Player> it = players.iterator();
//            while (it.hasNext()) {
//                Player p = it.next();
//                if (p.getGameMode().getValue() != mode) {
//                    it.remove();
//                }
//            }
//        }
//
//        if (args.get("l") != null) {
//            int level = Integer.parseInt(args.get("l"));
//            Iterator<Player> it = players.iterator();
//            while (it.hasNext()) {
//                Player p = it.next();
//                if (p.getLevel() > level) {
//                    it.remove();
//                }
//            }
//        }
//
//        if (args.get("lm") != null) {
//            int level = Integer.parseInt(args.get("lm"));
//            Iterator<Player> it = players.iterator();
//            while (it.hasNext()) {
//                Player p = it.next();
//                if (p.getLevel() < level) {
//                    it.remove();
//                }
//            }
//        }
//
//        l.setX(args.containsKey("x") ? Double.parseDouble(args.get("x")) : l.getX());
//        l.setY(args.containsKey("y") ? Double.parseDouble(args.get("y")) : l.getY());
//        l.setZ(args.containsKey("z") ? Double.parseDouble(args.get("z")) : l.getZ());
//
//        // process distance
//        if (args.get("rm") != null) {
//            int rm = Integer.parseInt(args.get("rm"));
//            Iterator<Player> it = players.iterator();
//            while (it.hasNext()) {
//                Player p = it.next();
//                if (p.getLocation().distance(l) < rm) {
//                    it.remove();
//                }
//            }
//        }
//
//        if (args.get("r") != null) {
//            int r = Integer.parseInt(args.get("r"));
//            Iterator<Player> it = players.iterator();
//            while (it.hasNext()) {
//                Player p = it.next();
//                if (p.getLocation().distance(l) > r) {
//                    it.remove();
//                }
//            }
//        }
//
//        // kill processing now if non needed
//        if (players.size() == 0) {
//            return new ArrayList<Player>();
//        }
//
//        List<Player> result = new ArrayList<Player>();
//
//        if (type.equals("p")) {
//            Player pp = players.get(0);
//            double nd = Double.MAX_VALUE;
//            for (Player p : players) {
//                double d = p.getLocation().distance(l);
//                if (d < nd) {
//                    nd = d;
//                    pp = p;
//                }
//            }
//            result.add(pp);
//        }
//
//        if (type.equals("r")) {
//            Random r = new Random();
//            result.add(players.get(r.nextInt(players.size())));
//        }
//
//        if (type.equals("a")) {
//            result.addAll(players);
//            if (args.get("c") != null) {
//                int c = Integer.parseInt(args.get("c"));
//                while (result.size() > c) {
//                    result.remove(c);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public static void main(String[] args) {
//        parseArgument("@p[r=5,z=1]", null);
//
//    }
//}
