package dev.sim0n.anticheat.player.tracker.impl.movement;

import dev.sim0n.anticheat.check.CheckData;
import dev.sim0n.anticheat.net.packet.ClientBoundPacket;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketEntityVelocity;
import dev.sim0n.anticheat.net.packet.system.impl.clientbound.SPacketPosLook;
import dev.sim0n.anticheat.net.packet.system.impl.serverbound.CPacketPlayer;
import dev.sim0n.anticheat.player.PlayerData;
import dev.sim0n.anticheat.player.tracker.PlayerTracker;
import dev.sim0n.anticheat.util.BBUtil;
import dev.sim0n.anticheat.util.BlockUtil;
import dev.sim0n.anticheat.util.NmsUtil;
import dev.sim0n.anticheat.util.PacketHelper;
import dev.sim0n.anticheat.util.data.CustomLocation;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MovementTracker extends PlayerTracker {
    private final MovementData data = new MovementData();

    private CustomLocation lastLocation, lastLastLocation;

    private Vector teleportPosition;

    private Vector velocity;

    private int velocityH, velocityV;
    private int velocityTicks;

    private int teleportTicks;

    public MovementTracker(PlayerData playerData) {
        super(playerData);

        registerOutgoingPreHandler(SPacketPosLook.class, packet -> {
            teleportPosition = new Vector(packet.getX(), packet.getY(), packet.getZ());
        }, ClientBoundPacket.PLAY_POSITION.getId());

        registerIncomingPreHandler(CPacketPlayer.class, packet -> {
            if (teleportPosition != null) {
                // The player packet the client sends after a teleport always has on ground set to false
                if (!packet.isOnGround()) {
                    // Since the positions matched this is a teleport
                    if (packet.getX() == teleportPosition.getX() && packet.getY() == teleportPosition.getY() && packet.getZ() == teleportPosition.getZ()) {
                        teleportPosition = null;
                        teleportTicks = 0;
                    }
                }
            }

            playerData.setTicksExisted(playerData.getTicksExisted() + 1);

            CustomLocation customLocation = new CustomLocation(
                    packet.getX(), packet.getY(), packet.getZ(),
                    packet.getYaw(), packet.getPitch(), packet.isOnGround()
            );

            boolean moving = packet.isMoving();
            boolean rotating = packet.isRotating();

            if (lastLocation != null) {
                if (!moving) {
                    customLocation.setX(lastLocation.getX());
                    customLocation.setY(lastLocation.getY());
                    customLocation.setZ(lastLocation.getZ());
                }

                if (!rotating) {
                    customLocation.setYaw(lastLocation.getYaw());
                    customLocation.setPitch(lastLocation.getPitch());
                }

                CheckData checkData = playerData.getCheckData();

                Player player = playerData.getBukkitPlayer();

                boolean flying = player.getGameMode() == GameMode.CREATIVE || player.isFlying() && player.getAllowFlight();

                if (flying)
                    velocityH = Math.max(20, velocityH);

                if (moving && customLocation.distanceSquared(lastLocation) > 0D && !flying) {
                    World world = NmsUtil.getEntityPlayer(player).world;

                    AxisAlignedBB bb = customLocation.toCollisionBox().getAABB();

                    // We need to get all colliding bounding boxes and then verify that they collide with the player
                    Set<AxisAlignedBB> bbs = BlockUtil.getCollidingBBs(world, bb).stream()
                            .filter(b -> BBUtil.isCollided(bb, b))
                            .collect(Collectors.toSet());

                    double targetY = customLocation.getY();

                    boolean collided = bbs.size() > 0;

                    boolean onGround = BBUtil.isCollidedBelow(bbs, targetY);
                    boolean onLadder = BlockUtil.isOnLadder(world, customLocation);
                    boolean inLiquid = BlockUtil.isInLiquid(world, customLocation);
                    boolean underBlock = BBUtil.isCollidedAbove(bbs, targetY + 1.8);

                    data.handle(collided, onGround, underBlock, onLadder, inLiquid);

                    if (velocityH > 0 && (customLocation.getX() != lastLocation.getX() || customLocation.getZ() != lastLocation.getZ()))
                        --velocityH;

                    if (velocityV > 0 && customLocation.getY() != lastLocation.getY())
                        --velocityV;

                    if (velocity != null && velocityH + velocityV == 0)
                        velocity = null;

                    checkData.getPositionChecks().forEach(check -> check.handle(customLocation, lastLocation, data));
                }

                if (rotating && (customLocation.getYaw() != lastLocation.getYaw() || customLocation.getPitch() != lastLocation.getPitch())) {
                    checkData.getRotationChecks().forEach(check -> check.handle(customLocation, lastLocation));
                }
            }

            if (lastLocation != null)
                lastLastLocation = lastLocation.clone();

            lastLocation = customLocation.clone();
        }, PacketHelper.FLYING_IDS);

        registerIncomingPostHandler(CPacketPlayer.class, packet -> {
            ++teleportTicks;
        }, PacketHelper.FLYING_IDS);

        registerOutgoingPreHandler(SPacketEntityVelocity.class, packet -> {
            if (packet.getEntityId() == playerData.getEntityId()) {
                playerData.sendTransaction((uid) -> {
                    velocity = new Vector(packet.getMotionX() / 8000D, packet.getMotionY() / 8000D, packet.getMotionZ() / 8000D);

                    velocityH = (int) Math.ceil((Math.abs(velocity.getX()) + Math.abs(velocity.getZ())) / 2D + 2D) * 4;
                    velocityV = (int) Math.ceil(Math.pow(Math.abs(velocity.getY()) + 2, 2)) * 2;

                    velocityTicks = (velocityH + velocityV) / 2;
                });
            }
        }, ClientBoundPacket.PLAY_ENTITY_VELOCITY.getId());
    }

    public boolean isTeleporting() {
        return teleportTicks <= 1;
    }

    public boolean isTeleportPending() {
        return teleportPosition != null;
    }

}
