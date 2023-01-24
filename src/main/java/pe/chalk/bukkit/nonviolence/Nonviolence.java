package pe.chalk.bukkit.nonviolence;

import org.bstats.bukkit.Metrics;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Nonviolence extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        new Metrics(this, 17521);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (DamageFilter.ALL.test(event)) {
            event.setCancelled(true);
            if (event.getEntity() instanceof LivingEntity target) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 1));
            }
        }
    }
}
