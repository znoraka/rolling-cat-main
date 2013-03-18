package fr.lirmm.smile.rollingcat.model.event;


public interface EventModel {
	public static final String start_game_event_type = "session_start";
	public static final String player_cursor_event_type="player_position";
	public static final String pointing_task_start="pointing_task_start";
	public static final String pointing_task_end="pointing_task_end";
	public static final String pointing_task_position="pointing_task_position";
	public static final String pointing_task_set="pointing_task_set";
	public static final String end_game_event_type = "session_end";
	public static final String game_info_event_type = "game_info";
	
	public String getID();
	public Long getTimeStamp();
	public String getType();
	public String getParameters();
	public String getCreationDate();
	public String getSessionID();
}
