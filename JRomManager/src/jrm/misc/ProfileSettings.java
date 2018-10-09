package jrm.misc;

import jrm.profile.scan.options.HashCollisionOptions;
import jrm.profile.scan.options.MergeOptions;

public class ProfileSettings extends Settings
{
	/**
	 * The merge mode used while filtering roms/disks
	 */
	public transient MergeOptions merge_mode;
	/**
	 * Must we strictly conform to merge tag (explicit), or search merge-able ROMs by ourselves (implicit)
	 */
	public transient Boolean implicit_merge;
	/**
	 * What hash collision mode is used?
	 */
	public transient HashCollisionOptions hash_collision_mode;
	

	public ProfileSettings()
	{
		// TODO Auto-generated constructor stub
	}

}