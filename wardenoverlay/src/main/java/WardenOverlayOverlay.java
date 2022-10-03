import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;
import net.unethicalite.api.entities.NPCs;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.entities.TileObjects;
import net.unethicalite.client.Static;
import net.unethicalite.wardenoverlay.WardenOverlayConfig;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashMap;

@Slf4j
@Singleton
public class WardenOverlayOverlay extends Overlay {


    private final WardenOverlayConfig config;

    private final Client client;
    private final HashMap<Integer, Integer> match;

    @Inject
    public WardenOverlayOverlay(Client client, WardenOverlayConfig config) {
        this.config = config;
        this.client = client;

        match = new HashMap<>();

        initTiles();

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.HIGHEST);
    }

    private void initTiles()
    {
        match.put(45372,45363);
        match.put(45368,45359);
        match.put(45373,45364);
        match.put(45370,45361);
        match.put(45366,45357);
    }

    private boolean babaCheck()
    {
        return NPCs.getNearest(a -> a.getName().contains("Ba-ba")) != null;
    }

    private boolean wardenCheck()
    {
        return NPCs.getNearest(a -> a.getName().contains("Warden")) != null;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if(config.enableMemoryPuzzle())
        {
            for (int i : match.keySet())
            {
                if (TileObjects.getNearest(i) != null && TileObjects.getNearest(match.get(i)) != null)
                {
                    renderTile(graphics, TileObjects.getNearest(match.get(i)).getLocalLocation(), Color.red);
                }
            }
        }

        for (GraphicsObject g : client.getGraphicsObjects())
        {
            if (config.enableFallingBoulder())
            {
                if (g.getId() == 2250 || g.getId() == 2251)
                {
                    renderTile(graphics, g.getLocation(), config.babaFallingBoulderColor());
                }
            }

                if (config.enableSlamOverlay())
                {
                    if (g.getId() == 2111 || g.getId() == 1446 || g.getId() == 1447 || g.getId() == 1448)
                    {
                        renderTile(graphics, g.getLocation(), config.babaSlamColor());
                    }
                }

                if (config.enableWardenSkull())
                {
                    if (g.getId() == 1447)
                    {
                        renderTile(graphics, g.getLocation(), config.wardenSkullColor());
                    }
                }
                if (config.enableWardenFlip())
                {
                    if (g != null && g.getLocation() != null && (g.getId() == 2220 || g.getId() == 2221 || g.getId() == 2222 || g.getId() == 2223))
                    {
                        renderTile(graphics, g.getLocation(), config.wardenFlipColor());
                    }
                }

                if (config.enablelightning())
                {
                    if (g.getId() == 1446)
                    {
                        renderTile(graphics, g.getLocation(), config.wardenPrelightningColor());
                    }
                    if (g.getId() == 2197)
                    {
                        renderTile(graphics, g.getLocation(), config.wardenlightningColor());
                    }
                }
            }

        if (config.enableWardenCore())
        {
            for (Projectile p : client.getProjectiles())
            {
                if (p.getId() == 2239 || p.getId() == 2225)
                {
                    renderTile(graphics, p.getTarget(), config.wardenCoreColor());
                }
            }
        }
        return null;
    }

    private void renderTile(final Graphics2D graphics, final LocalPoint dest, final Color color)
    {
        if (dest == null)
        {
            return;
        }

        final Polygon poly = Perspective.getCanvasTilePoly(Static.getClient(), dest);

        if (poly == null)
        {
            return;
        }

        OverlayUtil.renderPolygon(graphics, poly, color);
    }
}
