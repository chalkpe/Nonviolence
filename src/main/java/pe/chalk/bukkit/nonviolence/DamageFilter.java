package pe.chalk.bukkit.nonviolence;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

public final class DamageFilter {
    // filter by victim
    public static final Predicate<EntityDamageEvent> VICTIM_HAS_CUSTOM_NAME = byVictim(victim -> Objects.nonNull(victim.getCustomName()));
    public static final Predicate<EntityDamageEvent> VICTIM_IS_TAMED = byVictim(victim -> victim instanceof Tameable tameable && tameable.isTamed());
    public static final Predicate<EntityDamageEvent> VICTIM_IS_VILLAGER = byVictim(victim -> victim instanceof Villager || victim instanceof IronGolem);

    // filter by attacker
    public static final Predicate<EntityDamageEvent> ATTACKED_BY_PLAYER_ITSELF = byAttacker(attacker -> attacker instanceof Player);
    public static final Predicate<EntityDamageEvent> ATTACKED_BY_PLAYER_PROJECTILE = byAttacker(attacker -> attacker instanceof Projectile p && p.getShooter() instanceof Player);
    public static final Predicate<EntityDamageEvent> ATTACKED_BY_PLAYER = ATTACKED_BY_PLAYER_ITSELF.or(ATTACKED_BY_PLAYER_PROJECTILE);

    // filter by cause
    public static final Predicate<EntityDamageEvent> CAUSED_BY_FALL = byCauses(DamageCause.FALL);
    public static final Predicate<EntityDamageEvent> CAUSED_BY_CHEAT = byCauses(DamageCause.VOID, DamageCause.SUICIDE);

    // combined filter
    public static final Predicate<EntityDamageEvent> SHOULD_CANCEL_DAMAGE = anyMatch(
            VICTIM_IS_TAMED,
            VICTIM_HAS_CUSTOM_NAME.and(not(ATTACKED_BY_PLAYER)),
            VICTIM_IS_VILLAGER.and(CAUSED_BY_FALL.or(ATTACKED_BY_PLAYER))
    );

    // final filter
    public static final Predicate<EntityDamageEvent> ALL = not(CAUSED_BY_CHEAT).and(SHOULD_CANCEL_DAMAGE);

    private DamageFilter() {
        throw new UnsupportedOperationException();
    }

    private static Predicate<EntityDamageEvent> byVictim(final Predicate<Entity> filter) {
        return event -> filter.test(event.getEntity());
    }

    private static Predicate<EntityDamageEvent> byAttacker(final Predicate<Entity> filter) {
        return event -> event instanceof EntityDamageByEntityEvent attackEvent && filter.test(attackEvent.getDamager());
    }

    private static Predicate<EntityDamageEvent> byCauses(final DamageCause... causes) {
        return event -> Arrays.asList(causes).contains(event.getCause());
    }

    @SafeVarargs
    private static Predicate<EntityDamageEvent> anyMatch(final Predicate<EntityDamageEvent>... filters) {
        return event -> Arrays.stream(filters).anyMatch(filter -> filter.test(event));
    }
}
