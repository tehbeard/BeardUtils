package com.tehbeard.utils.cuboid.selector;

import com.tehbeard.utils.Vec3;
import com.tehbeard.utils.cuboid.Cuboid;
import com.tehbeard.utils.cuboid.selector.CuboidSelector.StatusIndicator.Activity;
import com.tehbeard.utils.session.SessionStore;
import java.util.HashSet;
import java.util.Set;

/**
 * Standard class for selecting a region of a world (cuboid) Relies on
 * SessionStore and Cuboid packages
 *
 * @author James
 *
 */
public class CuboidSelector {

    private final SessionStore<Cuboid> session = new SessionStore<Cuboid>();
    StatusIndicator indicator;

    private final Set<String> active = new HashSet<String>();

    public CuboidSelector(StatusIndicator indicator) {
        this.indicator = indicator;
    }

    private void indicate(Activity activity, String player, Cuboid cuboid) {
        if (this.indicator != null) {
            this.indicator.cuboidUpdate(activity, player, cuboid);
        }
    }

    public void setActive(String player) {
        this.active.add(player);

        this.session.putSession(player, new Cuboid());
        indicate(Activity.ACTIVE, player, null);
    }

    public void setInActive(String player) {
        this.active.remove(player);
        this.session.clearSession(player);
        indicate(Activity.INACTIVE, player, null);
    }

    public boolean isActive(String player) {
        return this.active.contains(player);
    }

    public boolean toggle(String player) {
        if (isActive(player)) {
            setInActive(player);
        } else {
            setActive(player);
        }

        return isActive(player);
    }

    public boolean leftClick(String player, Vec3 vec) {
        if (!isActive(player)) {
            return false;
        }
        this.session.getSession(player).setV1(vec);
        indicate(Activity.SELECT_CORNER_ONE, player, this.session.getSession(player));
        return true;
    }

    public boolean rightClick(String player, Vec3 vec) {
        if (!isActive(player)) {
            return false;
        }
        this.session.getSession(player).setV1(vec);
        indicate(Activity.SELECT_CORNER_TWO, player, this.session.getSession(player));
        return true;
    }

    public Cuboid getCuboid(String player) {
        return this.session.getSession(player);

    }

    public interface StatusIndicator {

        public enum Activity {

            SELECT_CORNER_ONE, SELECT_CORNER_TWO, ACTIVE, INACTIVE
        }

        public void cuboidUpdate(Activity activity, String player, Cuboid cuboid);
    }
}
