package game.jewels;

import game.Global;

public class Diamond
    extends Jewel {

    public Diamond() { super(Global.sprites[0]); }

    @Override public String toString() { return "Diamond"; }
}