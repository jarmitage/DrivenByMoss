// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.launchkey;

import de.mossgrabers.framework.controller.DefaultControllerDefinition;
import de.mossgrabers.framework.utils.OperatingSystem;
import de.mossgrabers.framework.utils.Pair;

import java.util.List;
import java.util.UUID;


/**
 * Definition class for the Novation Launchkey Mini Mk3 controller extension.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class LaunchkeyMiniMk3ControllerDefinition extends DefaultControllerDefinition
{
    private static final UUID EXTENSION_ID = UUID.fromString ("5359D5B1-28CD-4457-B49D-F8D3D7BC52B9");


    /**
     * Constructor.
     */
    public LaunchkeyMiniMk3ControllerDefinition ()
    {
        super (EXTENSION_ID, "Launchkey Mini Mk3", "Novation", 2, 1);
    }


    /** {@inheritDoc} */
    @Override
    public List<Pair<String [], String []>> getMidiDiscoveryPairs (final OperatingSystem os)
    {
        final List<Pair<String [], String []>> midiDiscoveryPairs = super.getMidiDiscoveryPairs (os);
        midiDiscoveryPairs.add (this.addDeviceDiscoveryPair (new String []
        {
            "MIDIIN2 (Launchkey Mini MK3)",
            "Launchkey Mini MK3"
        }, new String []
        {
            "MIDIOUT2 (Launchkey Mini MK3)"
        }));
        return midiDiscoveryPairs;
    }
}
