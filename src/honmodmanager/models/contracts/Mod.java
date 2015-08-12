package honmodmanager.models.contracts;

/**
 * Represent a game mod.
 *
 * @author Burgy Benjamin
 */
public interface Mod
{
    /**
     * Gets the mod name.
     *
     * @return The mod name.
     */
    String getName();

    /**
     * Gets the mod version.
     *
     * @return The mod version.
     */
    Version getVersion();
}
