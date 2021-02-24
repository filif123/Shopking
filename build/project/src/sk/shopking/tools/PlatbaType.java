/**
 * 
 */
package sk.shopking.tools;

/**
 * Určuje typ platby prevedeného nákupu.
 * @author Filip
 *
 */
public enum PlatbaType {
	/**
	 * Platba hotovosťou
	 */
	HOTOVOST,
	/**
	 * Platba kartou
	 * @apiNote NOT USED
	 */
	KARTA,
	/**
	 * Platba stravnými lístkami
	 */
	STRAVNE_LISTKY;
}
