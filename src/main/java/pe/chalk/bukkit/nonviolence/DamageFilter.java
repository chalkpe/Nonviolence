package pe.chalk.bukkit.nonviolence;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.function.Predicate;

public final class DamageFilter {
    public static final Predicate<EntityDamageEvent> IS_TAMED = event -> event.getEntity() instanceof Tameable tameable && tameable.isTamed();
    public static final Predicate<EntityDamageEvent> IS_VILLAGER = event -> event.getEntity() instanceof Villager || event.getEntity() instanceof IronGolem;
    public static final Predicate<EntityDamageEvent> CAUSED_BY_FALL = event -> event.getCause() == EntityDamageEvent.DamageCause.FALL;
    public static final Predicate<EntityDamageEvent> ATTACKED_BY_PLAYER = event -> event instanceof EntityDamageByEntityEvent e && (e.getDamager() instanceof Projectile p ? p.getShooter() : e.getDamager()) instanceof Player;
    public static final Predicate<EntityDamageEvent> ALL = IS_TAMED.or(IS_VILLAGER.and(CAUSED_BY_FALL.or(ATTACKED_BY_PLAYER)));

    private DamageFilter() {
        throw new UnsupportedOperationException();
    }
}
