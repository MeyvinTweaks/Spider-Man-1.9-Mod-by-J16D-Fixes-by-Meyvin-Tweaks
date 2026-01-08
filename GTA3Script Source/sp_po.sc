// by J16D
// Powers
// Spider-Man Mod for GTA SA c.2018 - 2022
// Addons by Meyvin Tweaks
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//POWERS
CONST_INT web_blossom 1
CONST_INT holo_decoy 2
CONST_INT bullet_proof 3
CONST_INT spider_bro 4
CONST_INT negative_shockwave 5
CONST_INT electric_punch 6
CONST_INT rock_out 7
CONST_INT blur_projector 8
CONST_INT low_gravity 9
CONST_INT iron_arms 10
CONST_INT defence_shield 11
CONST_INT spirit_fire 12
CONST_INT quips 13
CONST_INT equalizer 14
CONST_INT quad_damage 15
CONST_INT king_of_the_ring 16

CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_po
WAIT 0
WAIT 0
WAIT 0
WAIT 0
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu idPowers
LVAR_INT max_time cool_down_time
LVAR_INT i p iChar anim_seq fx_system sfx iObj
LVAR_INT counter iTempVar 
LVAR_INT lvar[2]
LVAR_FLOAT x[3] y[3] z[3] fRandomVal[2] fAngle[2] fDistance

GET_PLAYER_CHAR 0 player_actor
//idPowers = 1
//SET_CLEO_SHARED_VAR varIdPowers idPowers
//SET_CLEO_SHARED_VAR varPowersProgress 100

start_check:
GOSUB readVars
IF toggleSpiderMod = 0
    WHILE toggleSpiderMod = 0
        WAIT 0
        GOSUB readVars 
        IF toggleSpiderMod = 1
            BREAK
        ENDIF
    ENDWHILE
ENDIF

GOSUB get_power_max_time

main_loop:
    IF IS_PLAYER_PLAYING player
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0     //1:true 0: false
                
                //restore powers
                GET_CLEO_SHARED_VAR varPowersProgress (iTempVar)
                IF 99 > iTempVar
                    GOSUB get_power_max_time
                    timera = 0
                    //GET_CLEO_SHARED_VAR varIdPowers (idPowers)
                    WHILE 99 > iTempVar
                        /* Not needed, scripts will be reseted after close Menu
                        GET_CLEO_SHARED_VAR varIdPowers (iTempVar2)
                        IF NOT iTempVar2 = idPowers //Update Cool_Down_time -check if power has changed
                            GET_CLEO_SHARED_VAR varIdPowers (idPowers)
                            GOSUB get_power_max_time
                        ENDIF*/
                        CLEO_CALL linearInterpolation 0 (0 cool_down_time timera) (0 100) (iTempVar)
                        CLAMP_INT iTempVar 0 100 (iTempVar)
                        SET_CLEO_SHARED_VAR varPowersProgress iTempVar
                        
                        GOSUB readVars
                        IF toggleSpiderMod = 0 //TRUE
                            GOTO end_sp_po
                        ENDIF
                        //PRINT_FORMATTED_NOW "p:%i" 1 iTempVar   //DEBUG
                        IF timera >= cool_down_time
                            BREAK
                        ENDIF
                        WAIT 0
                    ENDWHILE
                    IF isInMainMenu = 0     //1:true 0: false
                        IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sp_prt.cs"
                            STREAM_CUSTOM_SCRIPT "SpiderJ16D\sp_prt.cs" 1   //ID:1  ||SUIT POWER READY
                            WAIT 1000
                        ENDIF
                    ENDIF
                ENDIF

                IF IS_CHAR_REALLY_IN_AIR player_actor
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOCK   // ~k~~PED_DUCK~
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOCK  // ~k~~PED_LOOKBEHIND~
                        IF CLEO_CALL is_power_ready 0
                            GET_CLEO_SHARED_VAR varIdPowers (idPowers)
                            IF idPowers = 9     //low_gravity    //id:9
                                CREATE_FX_SYSTEM_ON_CHAR SP_POWERS player_actor (0.0 0.0 0.15) 4 (fx_system)
                                PLAY_AND_KILL_FX_SYSTEM fx_system
                                WAIT 0
                                GOSUB get_power_max_time
                                GOSUB assign_low_gravity
                            ENDIF
                            IF idPowers = 13     //quips    //id:13
                                CREATE_FX_SYSTEM_ON_CHAR SP_POWERS player_actor (0.0 0.0 0.15) 4 (fx_system)
                                PLAY_AND_KILL_FX_SYSTEM fx_system
                                WAIT 0
                                GOSUB get_power_max_time
                                GOSUB assign_quips
                            ENDIF  
                            IF idPowers = 10     //ironarms    //id:10
                                CREATE_FX_SYSTEM_ON_CHAR SP_POWERS player_actor (0.0 0.0 0.15) 4 (fx_system)
                                PLAY_AND_KILL_FX_SYSTEM fx_system
                                WAIT 0
                                GOSUB get_power_max_time
                                GOSUB assign_iron_arms
                            ENDIF                                                                                 
                        ENDIF
                    ENDIF

                ELSE
                    //ground
                    IF IS_BUTTON_PRESSED PAD1 LEFTSHOCK   // ~k~~PED_DUCK~
                    AND IS_BUTTON_PRESSED PAD1 RIGHTSHOCK  // ~k~~PED_LOOKBEHIND~

                        IF CLEO_CALL is_power_ready 0 ()
                        AND GOSUB is_not_player_playing_finisher_anims
                            GOSUB REQUEST_Animations

                            GOSUB get_power_max_time
                            GET_CLEO_SHARED_VAR varIdPowers (idPowers)
                            IF idPowers >= 1
                            AND 16 >= idPowers
                                CREATE_FX_SYSTEM_ON_CHAR SP_POWERS player_actor (0.0 0.0 0.15) 4 (fx_system)
                                PLAY_AND_KILL_FX_SYSTEM fx_system
                                CLEAR_CHAR_SECONDARY_TASKS player_actor
                                TASK_PLAY_ANIM_SECONDARY player_actor ("pow_act_partial" "spider") 11.0 (0 1 1 0) -1
                                WAIT 0
                                //SET_CHAR_ANIM_SPEED player_actor "pow_act_partial" 2.0
                            ENDIF
                            SWITCH idPowers
                                CASE web_blossom    //id:1
                                    GOSUB assign_web_blossom
                                    BREAK
                                CASE holo_decoy     //id:2
                                    GOSUB assign_holo_decoy
                                    BREAK
                                CASE bullet_proof   //id:3
                                    GOSUB assign_bullet_proof
                                    BREAK
                                CASE spider_bro     //id:4
                                    GOSUB assign_spider_bro
                                    BREAK
                                CASE negative_shockwave   //id:5
                                    GOSUB assign_negative_shockwave
                                    BREAK
                                CASE electric_punch     //id:6
                                    GOSUB assign_electric_punch
                                    BREAK
                                CASE rock_out       //id:7
                                    GOSUB assign_rock_out
                                    BREAK
                                CASE blur_projector //id:8
                                    GOSUB assign_blur_projector
                                    BREAK 
                                CASE low_gravity    //id:9
                                    GOSUB assign_low_gravity
                                    BREAK
                                CASE iron_arms      //id:10
                                    GOSUB assign_iron_arms
                                    BREAK
                                CASE defence_shield //id:11
                                    GOSUB assign_defence_shield
                                    BREAK
                                CASE spirit_fire    //id:12
                                    GOSUB assign_spirit_fire
                                    BREAK
                                CASE quips    //id:13
                                    GOSUB assign_quips
                                    BREAK   
                                CASE equalizer    //id:14
                                    GOSUB assign_equalizer
                                    BREAK  
                                CASE quad_damage   //id:15
                                    GOSUB assign_quad_damage
                                    BREAK  
                                CASE king_of_the_ring    //id:16
                                    GOSUB assign_king_of_the_ring 
                                    BREAK                                                                                                                                           
                                DEFAULT
                                    WAIT 500
                                    BREAK
                            ENDSWITCH
                        ENDIF
                        WHILE IS_BUTTON_PRESSED PAD1 LEFTSHOCK   // ~k~~PED_DUCK~
                            WAIT 0
                        ENDWHILE
                        WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOCK  // ~k~~PED_LOOKBEHIND~
                            WAIT 0
                        ENDWHILE

                    ENDIF
                
                ENDIF

            ENDIF
        ELSE
            end_sp_po:
            WAIT 1000
            GOSUB readVars
            IF toggleSpiderMod = 0            
                REMOVE_AUDIO_STREAM sfx
                REMOVE_ANIMATION "spider"
                WAIT 0
                GOTO start_check
                //TERMINATE_THIS_CUSTOM_SCRIPT           
            ENDIF
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop  

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
RETURN

get_power_max_time:
    GET_CLEO_SHARED_VAR varIdPowers (idPowers)
    SWITCH idPowers
        CASE web_blossom    //id:1
            max_time = 10000        //none
            cool_down_time = 90000  //1:30     
            BREAK
        CASE holo_decoy     //id:2
            max_time = 30000        //0:30 
            cool_down_time = 60000  //1:00
            BREAK
        CASE bullet_proof   //id:3
            max_time = 30000          //0:30
            cool_down_time = 120000  //2:00
            BREAK
        CASE spider_bro     //id:4
            max_time = 15000        //0:15
            cool_down_time = 120000  //2:00
            BREAK
        CASE negative_shockwave   //id:5
            max_time = 10000    //none
            cool_down_time = 60000  //1:00      
            BREAK
        CASE electric_punch     //id:6
            max_time = 20000    //0:20
            cool_down_time = 90000  //1:30      
            BREAK
        CASE rock_out       //id:7
            max_time = 10000    //none
            cool_down_time = 60000  //1:00    
            BREAK
        CASE blur_projector //id:8
            max_time = 15000    //0:15
            cool_down_time = 60000  //1:00
            BREAK 
        CASE low_gravity    //id:9
            max_time = 10000    //0:10
            cool_down_time = 30000  //0:30
            BREAK
        CASE iron_arms      //id:10
            max_time = 165000    //1:05    65000
            cool_down_time = 120000  //2:00     120000
            BREAK
        CASE defence_shield //id:11
            max_time = 15000    //0:15
            cool_down_time = 120000  //2:00
            BREAK
        CASE spirit_fire    //id:12
            max_time = 15000    //0:15
            cool_down_time = 180000  //3:00
            BREAK
        CASE quips    //id:13
            max_time = 6500    //0:06
            cool_down_time = 120000  //2:00
            BREAK            
        CASE equalizer    //id:14
            max_time = 100000    //1:00
            cool_down_time = 180000  //3:00
            BREAK       
        CASE quad_damage   //id:15
            max_time = 20000    //0:20
            cool_down_time = 100000  //1:00
            BREAK  
        CASE king_of_the_ring   //id:16
            max_time = 65000    //1:05
            cool_down_time = 120000  //2:00
            BREAK                                
    ENDSWITCH
RETURN
//------------------------------------------------------------
assign_web_blossom:    //id:1
    CLEO_CALL set_current_power_progress 0 max_time max_time
    WAIT 0
    GOSUB play_sfx_web_blossom

    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pow_web_blossom" "spider") 135.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "pow_web_blossom" 1.25
    IF IS_CHAR_PLAYING_ANIM player_actor "pow_web_blossom"
        iTempVar = 0
        WHILE IS_CHAR_PLAYING_ANIM player_actor "pow_web_blossom"
            GET_CHAR_ANIM_CURRENT_TIME player_actor "pow_web_blossom" (fDistance)
            IF fDistance >= 0.104  //frame 14/134
                IF iTempVar = 0
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
                    GET_GROUND_Z_FOR_3D_COORD (x[1] y[1] z[1]) (z[1])
                    z[2] += 0.20
                    CREATE_FX_SYSTEM SP_BLOSSOM_A (x[1] y[1] z[1]) 1 (fx_system)
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                    iTempVar = 1
                ENDIF
                //counter = 0
                IF GOSUB is_char_around_player
                    IF NOT IS_CHAR_IN_ANY_CAR iChar
                    AND NOT IS_CHAR_ON_ANY_BIKE iChar
                    AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                        IF IS_CHAR_ON_SCREEN iChar 
                            IF GOSUB is_not_playing_anim_a
                                GOSUB task_play_hit_blossom
                                GET_CLEO_SHARED_VAR varHitCount counter       
                                counter ++
                                SET_CLEO_SHARED_VAR varHitCount counter                                                           
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
            IF fDistance >= 0.776  //frame 104/134
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        WAIT 50
    ENDIF
    WAIT 0
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
RETURN

is_char_around_player:
    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
    IF GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 7.0 0 1 (iChar) 
        IF DOES_CHAR_EXIST iChar
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

task_play_hit_blossom:
    GOSUB REQUEST_Animations
    //show fx
    CREATE_FX_SYSTEM_ON_CHAR SP_HIT_WEB iChar (0.0 0.25 0.5) 4 (fx_system)
    PLAY_AND_KILL_FX_SYSTEM fx_system
    GOSUB assign_char_reference

    //set char z angle
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.25) (x[1] y[1] z[1])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.25) (x[0] y[0] z[0])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fDistance)
    SET_CHAR_HEADING iChar fDistance
    //anims
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    //TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("pow_hit_wblm" "spider") 121.0 (0 1 1 0) -1
    TASK_DIE_NAMED_ANIM iChar "pow_hit_wblm" "spider" 121.0 5000
    WAIT 0
    IF IS_CHAR_PLAYING_ANIM iChar "pow_hit_wblm"
        WHILE IS_CHAR_PLAYING_ANIM iChar ("pow_hit_wblm")
            GET_CHAR_ANIM_CURRENT_TIME iChar ("pow_hit_wblm") (fDistance)
            IF fDistance >= 0.125   // frame 15/120 
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        CREATE_FX_SYSTEM_ON_CHAR SP_WEB_G iChar (0.0 0.0 -0.75) 1 (fx_system)
        PLAY_AND_KILL_FX_SYSTEM fx_system
    ENDIF    
    IF DOES_CHAR_EXIST iChar
        IF IS_CHAR_SCRIPT_CONTROLLED iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar          
        ENDIF
    ENDIF
    WAIT 0
RETURN

is_not_playing_anim_a:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")  //Web Shoot
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        IF NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("pow_hit_wblm") //hitted fall
            RETURN_TRUE
            RETURN
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

play_sfx_web_blossom:
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_wb1.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_wb1.mp3" (sfx)
        SET_AUDIO_STREAM_STATE sfx 1    //play
        GET_AUDIO_SFX_VOLUME (fRandomVal[1])
        fRandomVal[1] *= 0.8
        SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
    ENDIF
    WAIT 0
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_holo_decoy:     //id:2
    //max_time = 10000    //ms
    timera = 0
    timerb = 0
    iTempVar = 0
    GOSUB play_sfx_holo_decoy

    GENERATE_RANDOM_FLOAT_IN_RANGE 3.0 4.0 (fRandomVal[0])  //x
    GENERATE_RANDOM_FLOAT_IN_RANGE -0.5 4.0 (fRandomVal[1])  //y

    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (fRandomVal[0] fRandomVal[1] 0.0) (x[0] y[0] z[0])
    CREATE_FX_SYSTEM SP_HOLO (x[0] y[0] z[0]) 1 (lvar[0])
    PLAY_FX_SYSTEM lvar[0]

    fRandomVal[0] *= -1.0
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (fRandomVal[0] fRandomVal[1] 0.0) (x[1] y[1] z[1])
    CREATE_FX_SYSTEM SP_HOLO (x[1] y[1] z[1]) 1 (lvar[1])
    PLAY_FX_SYSTEM lvar[1]
    iTempVar = 2

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    IF IS_CHAR_ON_SCREEN iChar 

                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[0] y[0] z[0] 4.0
                            IF NOT IS_CHAR_FALLEN_ON_GROUND iChar
                                IF GOSUB is_not_playing_anim_b
                                    IF NOT IS_CHAR_USING_GUN iChar
                                        GOSUB assign_melee_task_at_coords_a
                                    ELSE
                                        GOSUB assign_shoot_task_at_coords_a
                                    ENDIF
                                ENDIF
                            ENDIF
                        ELSE
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[1] y[1] z[1] 4.0
                                IF NOT IS_CHAR_FALLEN_ON_GROUND iChar
                                    IF GOSUB is_not_playing_anim_b
                                        IF NOT IS_CHAR_USING_GUN iChar
                                            GOSUB assign_melee_task_at_coords_b
                                        ELSE
                                            GOSUB assign_shoot_task_at_coords_b
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF

                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE

        //speak audio
        IF timerb >= 2500
        AND 4000 >= timerb
            IF iTempVar = 2
                GOSUB play_sfx_holo_decoy
                iTempVar = 3
            ENDIF
        ELSE
            IF iTempVar = 3
                GOSUB play_sfx_holo_decoy
                iTempVar = 2
                timerb = 0
            ENDIF            
        ENDIF
        IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 30.0
            IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[1] y[1] z[1] 30.0
                BREAK
            ENDIF
        ENDIF

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE
    iTempVar = 1
    GOSUB play_sfx_holo_decoy
    KILL_FX_SYSTEM lvar[0]
    KILL_FX_SYSTEM lvar[1]
    WAIT 25
    REMOVE_AUDIO_STREAM sfx
RETURN

set_z_angle_holo_a:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[0] y[0]) (fRandomVal[0])
    SET_CHAR_HEADING iChar fRandomVal[0]
RETURN

assign_melee_task_at_coords_a:
    IF NOT IS_CHAR_PLAYING_ANIM iChar "EV_step"
    AND NOT IS_CHAR_PLAYING_ANIM iChar "IDLE_chat"
        GOSUB assign_char_reference
        
        GOSUB set_z_angle_holo_a
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        OPEN_SEQUENCE_TASK anim_seq
            TASK_LOOK_AT_COORD -1 x[0] y[0] z[0] -1
            TASK_PLAY_ANIM -1 ("EV_step" "ped") 33.0 (0 1 1 0) -1
            TASK_PLAY_ANIM -1 ("IDLE_chat" "ped") 201.0 (0 1 1 0) -1
        CLOSE_SEQUENCE_TASK anim_seq
        PERFORM_SEQUENCE_TASK iChar anim_seq
        WAIT 1
        CLEAR_SEQUENCE_TASK anim_seq
    ENDIF
RETURN

assign_shoot_task_at_coords_a:
    IF NOT IS_CHAR_PLAYING_ANIM iChar "EV_step"
    AND NOT IS_CHAR_PLAYING_ANIM iChar "IDLE_chat"
        GOSUB assign_char_reference

        GOSUB set_z_angle_holo_a
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        OPEN_SEQUENCE_TASK anim_seq
            TASK_COWER -1
            TASK_AIM_GUN_AT_COORD -1 x[0] y[0] z[0] 500
            TASK_SHOOT_AT_COORD -1 x[0] y[0] z[0] 2000
        CLOSE_SEQUENCE_TASK anim_seq
        PERFORM_SEQUENCE_TASK iChar anim_seq
        WAIT 1
        CLEAR_SEQUENCE_TASK anim_seq
    ENDIF
RETURN

set_z_angle_holo_b:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[2] y[2] z[2])
    GET_ANGLE_FROM_TWO_COORDS (x[2] y[2]) (x[1] y[1]) (fRandomVal[0])
    SET_CHAR_HEADING iChar fRandomVal[0]
RETURN

assign_melee_task_at_coords_b:
    IF NOT IS_CHAR_PLAYING_ANIM iChar "EV_step"
    AND NOT IS_CHAR_PLAYING_ANIM iChar "IDLE_chat"
        GOSUB assign_char_reference

        GOSUB set_z_angle_holo_b
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        OPEN_SEQUENCE_TASK anim_seq
            TASK_LOOK_AT_COORD -1 x[1] y[1] z[1] -1
            TASK_PLAY_ANIM -1 ("EV_step" "ped") 33.0 (0 1 1 0) -1
            TASK_PLAY_ANIM -1 ("IDLE_chat" "ped") 201.0 (0 1 1 0) -1
        CLOSE_SEQUENCE_TASK anim_seq
        PERFORM_SEQUENCE_TASK iChar anim_seq
        WAIT 1
        CLEAR_SEQUENCE_TASK anim_seq
    ENDIF
RETURN

assign_shoot_task_at_coords_b:
    IF NOT IS_CHAR_PLAYING_ANIM iChar "EV_step"
    AND NOT IS_CHAR_PLAYING_ANIM iChar "IDLE_chat"
        GOSUB assign_char_reference
        
        GOSUB set_z_angle_holo_b
        CLEAR_CHAR_TASKS iChar
        CLEAR_CHAR_TASKS_IMMEDIATELY iChar
        OPEN_SEQUENCE_TASK anim_seq
            TASK_COWER -1
            TASK_AIM_GUN_AT_COORD -1 x[1] y[1] z[1] 1000
            TASK_SHOOT_AT_COORD -1 x[1] y[1] z[1] 2000
        CLOSE_SEQUENCE_TASK anim_seq
        PERFORM_SEQUENCE_TASK iChar anim_seq
        WAIT 1
        CLEAR_SEQUENCE_TASK anim_seq
    ENDIF
RETURN

is_lying_on_the_floor:
    IF IS_CHAR_PLAYING_ANIM iChar "KO_skid_front"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_skid_back"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_L"
    OR IS_CHAR_PLAYING_ANIM iChar "KO_spin_R"
    OR IS_CHAR_PLAYING_ANIM iChar "knife_hit_3"
    OR IS_CHAR_PLAYING_ANIM iChar "sp_wf_b"
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
RETURN

is_not_playing_anim_b:
    IF NOT IS_CHAR_PLAYING_ANIM iChar ("hit_wshoot_p")  //Web Shoot
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_we_a")  //Electric Web
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_wall")  //Impact Web, Web Shoot, Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("ko_ground") //Web Shoot, Web Bomb
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_a")  //Suspension Matrix
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_a")  //Trip Mine
    AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wh_b")  //Trip Mine
        IF NOT IS_CHAR_PLAYING_ANIM iChar ("dodge_front_b_hit")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("dodge_front_c_hit")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("dodge_front_c_hita")
        AND NOT IS_CHAR_PLAYING_ANIM iChar ("dodge_front_c_hitb")

            IF NOT IS_CHAR_PLAYING_ANIM iChar ("IDLE_chat")
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("EV_step")
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("pow_hit_wblm")     //hitted fall/hit shockwave
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h2")     //hitted rock out power
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("sp_wf_b")
            AND NOT IS_CHAR_PLAYING_ANIM iChar ("swing_kick_hit_1")     //swing kick hit

                IF NOT IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_6")   //fight anims
                AND NOT IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_5")
                AND NOT IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_7")
                AND NOT IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_4")

                    IF NOT IS_CHAR_PLAYING_ANIM iChar ("ground_to_air_hit") //Air combo anims
                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_1")
                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_fail")
                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_2")
                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_finish_1")
                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_3")

                        IF NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_3b")
                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_finish_2")
                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_4")
                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_finish_3")
                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("getup")
                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("air_combo_hit_4b")
                            RETURN_TRUE
                            RETURN
                        ENDIF

                    ENDIF
                ENDIF           
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

play_sfx_holo_decoy:
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0  //in
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_hd1.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_hd1.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
                //SET_AUDIO_STREAM_VOLUME sfx 0.8
            ENDIF
            BREAK
        CASE 1  //out
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_hd2.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_hd2.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 0.8
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
        CASE 2  //speak
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_hd4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                //SET_PLAY_3D_AUDIO_STREAM_AT_COORDS sfx x[0] y[0] z[0]
                SET_AUDIO_STREAM_STATE sfx 1    //play
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
        CASE 3  //speak
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_hd5.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                //SET_PLAY_3D_AUDIO_STREAM_AT_COORDS sfx x[1] y[1] z[1]
                SET_AUDIO_STREAM_STATE sfx 1    //play
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
    ENDSWITCH
    WAIT 0
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_bullet_proof:    //id:3
    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera
        SET_CHAR_PROOFS player_actor TRUE FALSE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee
        
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF IS_CHAR_ON_SCREEN iChar 
                    IF IS_CHAR_SHOOTING iChar
                        //CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_BP player_actor (0.0 0.20 0.15) (90.0 0.0 90.0) 4 (fx_system)
                        //PLAY_AND_KILL_FX_SYSTEM fx_system
                        GENERATE_RANDOM_FLOAT_IN_RANGE -0.9 0.9 fRandomVal[0]
                        GENERATE_RANDOM_FLOAT_IN_RANGE -1.0 1.0 fRandomVal[1]
                        GENERATE_RANDOM_FLOAT_IN_RANGE -0.1 0.75 fDistance
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor fRandomVal[0] fRandomVal[1] fDistance (x[0] y[0] z[0])
                        //GENERATE_RANDOM_INT_IN_RANGE 2 6 (iTempVar) // BONE_PELVIS - BONE_HEAD2
                        //CLEO_CALL getActorBonePos 0 player_actor iTempVar (x[0] y[0] z[0])
                        GENERATE_RANDOM_FLOAT_IN_RANGE -1.5 1.5 fRandomVal[0]
                        GENERATE_RANDOM_FLOAT_IN_RANGE -2.0 0.1 fRandomVal[1]
                        GENERATE_RANDOM_FLOAT_IN_RANGE -0.6 1.5 fDistance
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar fRandomVal[0] fRandomVal[1] fDistance (x[1] y[1] z[1])
                        IF IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (0 0 1 0 0) //solid|car|actor|object|particle
                            FIRE_SINGLE_BULLET (x[0] y[0] z[0]) (x[1] y[1] z[1]) 10
                        ENDIF
                        WAIT 10
                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE
    //PRINT_FORMATTED_NOW "off" 500
    REMOVE_AUDIO_STREAM sfx
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee
    WAIT 50
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_spider_bro:  //id:4
    //max_time = 15000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx

    GOSUB create_spider_bro
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 2.0 1.0 (x[0] y[0] z[0])
    CLEO_CALL setObjectPosSimple 0 iObj x[0] y[0] z[0]
    GET_CHAR_HEADING player_actor (fAngle[0])
    SET_OBJECT_HEADING iObj fAngle[0]
    SET_OBJECT_SCALE iObj 2.0
    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        IF DOES_OBJECT_EXIST iObj
            //store chars
            i = 0
            WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
                IF DOES_CHAR_EXIST iChar
                AND NOT IS_CHAR_DEAD iChar
                AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                    IF IS_CHAR_ON_SCREEN iChar 

                        IF IS_CHAR_DOING_TASK_ID iChar TASK_COMPLEX_KILL_PED_ON_FOOT
                        OR IS_CHAR_DOING_TASK_ID iChar TASK_COMPLEX_KILL_PED_ON_FOOT_MELEE
                        OR IS_CHAR_DOING_TASK_ID iChar TASK_KILL_ALL_THREATS
                        OR IS_CHAR_DOING_TASK_ID iChar TASK_KILL_PED_ON_FOOT_WHILE_DUCKING
                            GOSUB assign_task_kill
                        ELSE
                            IF IS_CHAR_DOING_TASK_ID iChar TASK_COMPLEX_KILL_PED_ON_FOOT_ARMED 
                            OR IS_CHAR_DOING_TASK_ID iChar TASK_KILL_PED_ON_FOOT_WHILE_DUCKING
                                GOSUB assign_task_kill
                            ENDIF
                        ENDIF

                    ENDIF
                ENDIF
            ENDWHILE

            IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 6.0
                WHILE NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 6.0
                    CLEO_CALL set_current_power_progress 0 max_time timera

                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[1] y[1] z[1])
                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 0.0 0.0 (x[0] y[0] z[0])
                    GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (fAngle[0])
                    SET_OBJECT_HEADING iObj fAngle[0]

                    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.5 0.0) (x[1] y[1] z[1])
                    GET_GROUND_Z_FOR_3D_COORD x[1] y[1] z[1] (z[1])
                    z[1] += 1.5
                    SLIDE_OBJECT iObj (x[1] y[1] z[1]) (6.0 6.0 6.0) FALSE
                    IF timera > max_time
                        BREAK
                    ENDIF
                    GOSUB readVars
                    IF toggleSpiderMod = 0  //FALSE
                    OR isInMainMenu = 1     //1:true 0:false
                        BREAK
                    ENDIF
                    WAIT 0
                ENDWHILE   
            ENDIF
            IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 40.0
                BREAK
            ENDIF

            // break loop
            GOSUB readVars
            IF toggleSpiderMod = 0  //FALSE
            OR isInMainMenu = 1     //1:true 0:false
                BREAK
            ENDIF
            IF NOT IS_PLAYER_PLAYING player
                BREAK
            ENDIF
            IF IS_CHAR_IN_ANY_CAR player_actor
                BREAK
            ENDIF
        ELSE
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE

    IF DOES_OBJECT_EXIST iObj
        KILL_FX_SYSTEM_NOW fx_system
        DELETE_OBJECT iObj
    ENDIF
    REMOVE_AUDIO_STREAM sfx
    //PRINT_FORMATTED_NOW "off" 500
    WAIT 50
RETURN

assign_task_kill:
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
        //get new (object) destination
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[1] y[1] z[1])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (fAngle[0])
        GET_DISTANCE_BETWEEN_COORDS_3D x[1] y[1] z[1] x[0] y[0] z[0] (fDistance)
        IF fDistance >= 3.0
            fDistance /= 2.0
        ELSE
            fDistance = 3.0
        ENDIF
        GET_COORD_FROM_ANGLED_DISTANCE x[0] y[0] fAngle[0] fDistance (x[1] y[1])
        //set object angle
        GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj 0.0 0.0 0.0 (x[0] y[0] z[0])
        GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (fAngle[0])
        SET_OBJECT_HEADING iObj fAngle[0]
        //throw object to new destination
        WHILE NOT LOCATE_OBJECT_DISTANCE_TO_COORDINATES iObj x[1] y[1] z[1] 1.5
            CLEO_CALL set_current_power_progress 0 max_time timera

            GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.35 0.0) (x[0] y[0] z[0])
            GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
            z[0] += 1.5
            SLIDE_OBJECT iObj (x[0] y[0] z[0]) (6.0 6.0 6.0) FALSE
            IF NOT LOCATE_CHAR_DISTANCE_TO_OBJECT player_actor iObj 30.0
                GOTO end_task_a
            ENDIF
            WAIT 0
        ENDWHILE
        //shot object angle to target
        GOSUB set_object_angle_to_target
        CONST_INT delay_shoot 300
        timerb = 0
        WHILE TRUE
            CLEO_CALL set_current_power_progress 0 max_time timera

            IF DOES_CHAR_EXIST iChar
                GOSUB set_object_angle_to_target
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
                GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.2 0.0) (x[0] y[0] z[0])
                IF timerb > delay_shoot
                    ADD_BIG_GUN_FLASH (x[0] y[0] z[0]) (x[1] y[1] z[1])
                    FIRE_SINGLE_BULLET (x[0] y[0] z[0]) (x[1] y[1] z[1]) 10
                    timerb = 0
                    GET_CLEO_SHARED_VAR varHitCount counter
                    counter ++
                    SET_CLEO_SHARED_VAR varHitCount counter
                ENDIF
                GET_DISTANCE_BETWEEN_COORDS_3D x[1] y[1] z[1] x[0] y[0] z[0] (fDistance)
                IF fDistance > 10.0
                OR NOT IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (0 1 0 1 0)    //buildings|cars|characters|objects|particles
                    BREAK
                ENDIF
            ELSE
                BREAK
            ENDIF
            IF IS_CHAR_DEAD iChar
                BREAK
            ENDIF
            IF timera > max_time
                BREAK
            ENDIF
            GOSUB readVars
            IF toggleSpiderMod = 0  //FALSE
            OR isInMainMenu = 1     //1:true 0:false
                BREAK
            ENDIF
            WAIT 0
        ENDWHILE
        end_task_a:
    ENDIF
RETURN

set_object_angle_to_target:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar 0.0 0.0 0.0 (x[1] y[1] z[1])
    GET_OFFSET_FROM_OBJECT_IN_WORLD_COORDS iObj (0.0 0.0 0.0) (x[0] y[0] z[0])
    CLEO_CALL getXangleBetweenPoints 0 (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fAngle[1])
    fAngle[1] *= -1.0
    GET_ANGLE_FROM_TWO_COORDS (x[0] y[0]) (x[1] y[1]) (fAngle[0])
    SET_OBJECT_ROTATION iObj (fAngle[1] 0.0 fAngle[0])
    SET_OBJECT_HEADING iObj fAngle[0]
RETURN

create_spider_bro:
    REQUEST_MODEL 6023  //pbro
    LOAD_ALL_MODELS_NOW
    CREATE_OBJECT_NO_SAVE 6023 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_PROOFS iObj 1 1 1 1 1    //BP FP EP CP MP
    SET_OBJECT_COLLISION iObj FALSE
    SET_OBJECT_VISIBLE iObj TRUE
    SET_OBJECT_DYNAMIC iObj FALSE
    MARK_MODEL_AS_NO_LONGER_NEEDED 6023
    CREATE_FX_SYSTEM_ON_OBJECT_WITH_DIRECTION JETPACK iObj (0.0 0.0 0.01) (90.0 0.0 0.0) 1 (fx_system)
    PLAY_FX_SYSTEM fx_system
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_negative_shockwave:  //id:5
    CLEO_CALL set_current_power_progress 0 max_time max_time

    iTempVar = 0
    GOSUB play_shockwave_Sfx
    WAIT 10
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pow_neg_shwave" "spider") 61.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "pow_neg_shwave" 0.7

    IF IS_CHAR_PLAYING_ANIM player_actor "pow_neg_shwave"

        WHILE IS_CHAR_PLAYING_ANIM player_actor "pow_neg_shwave"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("pow_neg_shwave") (fRandomVal[0])
            IF fRandomVal[0] >= 0.150 //frame 9/60
                SET_CHAR_ANIM_SPEED player_actor "pow_neg_shwave" 1.0
            ENDIF
            IF fRandomVal[0] >= 0.233 //frame 14/60
                IF iTempVar = 0
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
                    GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
                    z[0] += 0.06
                    CREATE_FX_SYSTEM SP_NEGATIVE_A (x[0] y[0] z[0]) 4 (lvar[0])
                    PLAY_AND_KILL_FX_SYSTEM lvar[0]
                    iTempVar = 1
                    GOSUB play_shockwave_Sfx
                ENDIF
            ENDIF
            IF fRandomVal[0] >= 0.233   //frame 14/60
            AND 0.500 > fRandomVal[0]  // frame 30/60

                GET_PED_POINTER player_actor (i)
                i += 0x47C
                READ_MEMORY i 4 0 (i)
                i += 0x130
                counter = 0
                WHILE 7 >= counter
                    READ_MEMORY i 4 0 (iChar)
                    IF NOT iChar = 0
                        //GET_PED_REF iChar (iChar)
                        READ_MEMORY 0x00B74490 4 0 (p)
                        CALL_METHOD_RETURN 0x4442D0 p /*nparms*/1 /*pop*/0 /*struct*/iChar /*store*/(iChar)         //FUNC_GET_ACTOR_ID
                //i = 0
                //WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
                        IF DOES_CHAR_EXIST iChar
                        AND NOT IS_CHAR_DEAD iChar
                        AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                            IF NOT IS_CHAR_IN_ANY_CAR iChar
                            AND NOT IS_CHAR_ON_ANY_BIKE iChar
                            AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                                IF IS_CHAR_ON_SCREEN iChar 
                                    IF LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 8.0
                                        IF GOSUB is_not_playing_anim_b
                                            GOSUB assign_task_hit_negative_shockwave
                                            GET_CLEO_SHARED_VAR varHitCount counter
                                            counter ++
                                            SET_CLEO_SHARED_VAR varHitCount counter                                            
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                //ENDWHILE
                    ENDIF
                    i += 0x4
                    counter += 1
                ENDWHILE
            ENDIF
            WAIT 0
        ENDWHILE

    ENDIF
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    WAIT 0
    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN

assign_task_hit_negative_shockwave:
    GOSUB assign_char_reference
    
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fRandomVal[0])
    SET_CHAR_HEADING iChar fRandomVal[0]
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    IF IS_CHAR_HEALTH_GREATER iChar 90
        DAMAGE_CHAR iChar 90 TRUE
        /*IF IS_CHAR_DEAD iChar
            TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500        //pow_hit_wblm
            WAIT 0
        ELSE*/
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("pow_rock_out_h2" "spider") 52.0 (0 1 1 0) -1        //pow_hit_wblm 121.0
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
            WAIT 0
            CLEAR_SEQUENCE_TASK anim_seq
        //ENDIF
    ELSE
        TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500    //pow_hit_wblm
        WAIT 0
    ENDIF
    IF IS_CHAR_PLAYING_ANIM iChar "pow_rock_out_h2"
        SET_CHAR_ANIM_SPEED iChar "pow_rock_out_h2" 1.50
    ENDIF
    WAIT 0
    IF IS_CHAR_DEAD iChar
        IF IS_CHAR_SCRIPT_CONTROLLED iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        ENDIF
    ENDIF
RETURN

play_shockwave_Sfx:
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_hd1.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_hd1.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 0.8
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.7
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
        CASE 1
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\wshot6a.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\wshot6a.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 0.65
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.65
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
    ENDSWITCH
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_electric_punch:
    //max_time = 8000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx

    CREATE_FX_SYSTEM_ON_CHAR SP_ELECTRIC_E player_actor (0.0 0.0 0.0) 1 (lvar[0])
    ATTACH_FX_SYSTEM_TO_CHAR_BONE lvar[0] player_actor 25   //25: BONE_RIGHTHAND
    PLAY_FX_SYSTEM lvar[0]
    CREATE_FX_SYSTEM_ON_CHAR SP_ELECTRIC_E player_actor (0.0 0.0 0.0) 1 (lvar[1])
    ATTACH_FX_SYSTEM_TO_CHAR_BONE lvar[1] player_actor 35   //35: BONE_LEFTHAND
    PLAY_FX_SYSTEM lvar[1]

    counter = 0
    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF IS_CHAR_ON_SCREEN iChar 
                    IF NOT IS_CHAR_FALLEN_ON_GROUND iChar
                        
                        IF GOSUB is_not_playing_anim_b
                            IF GOSUB is_playing_hit_anim
                                IF DOES_FILE_EXIST "CLEO\SpiderJ16D\we_1.cs"
                                    STREAM_CUSTOM_SCRIPT "SpiderJ16D\we_1.cs" iChar
                                    WAIT 50                                     
                                ENDIF                                 
                            ENDIF
                        ENDIF                        

                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        WAIT 0
    ENDWHILE   
    KILL_FX_SYSTEM_NOW lvar[0]
    KILL_FX_SYSTEM_NOW lvar[1]
    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN

is_playing_hit_anim:
    IF IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_1")
    OR IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_2")
    OR IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_3")
        RETURN_TRUE
    ELSE
        IF IS_CHAR_PLAYING_ANIM iChar ("HitA_1")
        OR IS_CHAR_PLAYING_ANIM iChar ("HitA_2")
        OR IS_CHAR_PLAYING_ANIM iChar ("HitA_3")
        OR IS_CHAR_PLAYING_ANIM iChar ("HIT_back")
        OR IS_CHAR_PLAYING_ANIM iChar ("HIT_behind")
        OR IS_CHAR_PLAYING_ANIM iChar ("HIT_front")        
            RETURN_TRUE
        ELSE
            IF IS_CHAR_PLAYING_ANIM iChar ("HIT_L")
            OR IS_CHAR_PLAYING_ANIM iChar ("HIT_R")
            OR IS_CHAR_PLAYING_ANIM iChar ("HIT_walk")
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ENDIF
    ENDIF
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_rock_out:    //id:7
    CLEO_CALL set_current_power_progress 0 max_time max_time
    WAIT 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    
    GOSUB REQUEST_Animations
    CLEAR_CHAR_TASKS player_actor
    CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    GOSUB create_spider_guitar
    WAIT 0
    TASK_PICK_UP_OBJECT player_actor iObj (0.0 0.05 0.0) 5 16 "NULL" "NULL" -1   //5 - left hand 
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("pow_rock_out" "spider") 70.0 (0 1 1 0) -1
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "pow_rock_out" 1.30

    IF IS_CHAR_PLAYING_ANIM player_actor "pow_rock_out"
        iTempVar = 2

        WHILE IS_CHAR_PLAYING_ANIM player_actor "pow_rock_out"
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("pow_rock_out") (fRandomVal[0])
            IF fRandomVal[0] >= 0.174 //frame 12/69
                IF iTempVar = 2
                    iTempVar = 0    //sfx guitar 0
                    GOSUB play_rock_out_sfx_a
                ENDIF
            ENDIF
            IF fRandomVal[0] >= 0.333 //frame 23/69
                SET_CHAR_ANIM_SPEED player_actor "pow_rock_out" 1.10
            ENDIF
            IF fRandomVal[0] >= 0.362 //frame 25/69
                IF iTempVar = 0
                    iTempVar = 1    //fall ground sfx
                    GOSUB play_sfx_general_sfx
                    CLEO_CALL setSmokeFX 0 player_actor (0.0 0.0 -0.5) 25.0

                    CREATE_FX_SYSTEM_ON_CHAR SP_ROCK_OUT player_actor (0.0 0.0 -0.45) 4 (fx_system)
                    PLAY_AND_KILL_FX_SYSTEM fx_system
                    WAIT 0
                    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.15 0.0) (x[0] y[0] z[0])
                    GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
                    z[0] += 0.1
                    CREATE_FX_SYSTEM SP_ROCK_OUT_B (x[0] y[0] z[0]) 4 (fx_system)
                    PLAY_AND_KILL_FX_SYSTEM fx_system

                    GOSUB play_rock_out_sfx_b   //sfx guitar 1
                ENDIF
            ENDIF
            IF fRandomVal[0] >= 0.797  //frame 55/69
                IF DOES_OBJECT_EXIST iObj
                    DELETE_OBJECT iObj
                ENDIF
            ENDIF
            i = 0
            WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
                IF DOES_CHAR_EXIST iChar
                AND NOT IS_CHAR_DEAD iChar
                AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                    IF NOT IS_CHAR_IN_ANY_CAR iChar
                    AND NOT IS_CHAR_ON_ANY_BIKE iChar
                    AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                        IF IS_CHAR_ON_SCREEN iChar 
                            IF LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 8.0
                                IF fRandomVal[0] >= 0.000  //frame 0/70
                                AND 0.323 > fRandomVal[0]  // frame 20/70
                                    IF NOT IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h1")  //hit complete sequence
                                    AND NOT IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h2")  //hit half sequence
                                        GOSUB assign_new_task_throw_away_anim
                                        GET_CLEO_SHARED_VAR varHitCount counter
                                        counter ++
                                        SET_CLEO_SHARED_VAR varHitCount counter                                           
                                    ENDIF
                                ELSE
                                    IF fRandomVal[0] >= 0.323  //frame 20/70    //20
                                    AND 0.694 > fRandomVal[0]  // frame 43/70   //50
                                        IF NOT IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h1")  //hit complete sequence
                                        AND NOT IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h2")  //hit half sequence
                                            GOSUB assign_new_task_throw_away_anim_b
                                        ENDIF
                                    ENDIF
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDWHILE
            WAIT 0
        ENDWHILE

    ENDIF
    CLEAR_CHAR_TASKS player_actor   //drop object
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    IF DOES_CHAR_EXIST iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        ENDIF
    ENDIF
    WAIT 1000
    REMOVE_AUDIO_STREAM sfx
    WAIT 0
    REMOVE_AUDIO_STREAM lvar[0]
    WAIT 0
    REMOVE_AUDIO_STREAM lvar[1]
    WAIT 50
RETURN

set_char_angle_point_player:
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fRandomVal[0])
    SET_CHAR_HEADING iChar fRandomVal[0]
RETURN

assign_new_task_throw_away_anim:
    GOSUB assign_char_reference
    WAIT 0
    GOSUB set_char_angle_point_player
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    IF IS_CHAR_HEALTH_GREATER iChar 90
        DAMAGE_CHAR iChar 90 TRUE
        /*IF IS_CHAR_DEAD iChar
            TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h1" "spider") 16.0 2500
            WAIT 0
        ELSE*/
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("pow_rock_out_h1" "spider") 52.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
            WAIT 0
            CLEAR_SEQUENCE_TASK anim_seq
        //ENDIF
    ELSE
        TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h1" "spider") 16.0 2500
        WAIT 0
    ENDIF
    IF IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h1")
        SET_CHAR_ANIM_SPEED iChar "pow_rock_out_h1" 1.50
    ENDIF
    WAIT 0
RETURN

assign_new_task_throw_away_anim_b:
    GOSUB assign_char_reference
    WAIT 0
    GOSUB set_char_angle_point_player
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    IF IS_CHAR_HEALTH_GREATER iChar 90
        DAMAGE_CHAR iChar 90 TRUE
        /*IF IS_CHAR_DEAD iChar
            TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500
            WAIT 0
        ELSE*/
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("pow_rock_out_h2" "spider") 52.0 (0 1 1 0) -1
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
            WAIT 0
            CLEAR_SEQUENCE_TASK anim_seq
        //ENDIF
    ELSE
        TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500
        WAIT 0
    ENDIF
    IF IS_CHAR_PLAYING_ANIM iChar ("pow_rock_out_h2")
        SET_CHAR_ANIM_SPEED iChar "pow_rock_out_h2" 1.50
    ENDIF
    WAIT 0
RETURN

create_spider_guitar:
    REQUEST_MODEL 6024  //sp_guitar
    LOAD_ALL_MODELS_NOW
    CREATE_OBJECT_NO_SAVE 6024 0.0 0.0 0.0 FALSE FALSE (iObj)
    SET_OBJECT_PROOFS iObj 1 1 1 1 1    //BP FP EP CP MP
    SET_OBJECT_COLLISION iObj FALSE
    SET_OBJECT_VISIBLE iObj TRUE
    SET_OBJECT_SCALE iObj 0.9
    MARK_MODEL_AS_NO_LONGER_NEEDED 6024

    GET_CHAR_HEADING player_actor (fAngle[0])
    SET_OBJECT_HEADING iObj fAngle[0]
RETURN

play_rock_out_sfx_a:
    REMOVE_AUDIO_STREAM lvar[0]
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_ro1.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_ro1.mp3" (lvar[0])
        SET_AUDIO_STREAM_STATE lvar[0] 1    //play
        //SET_AUDIO_STREAM_VOLUME lvar[0] 0.8
        GET_AUDIO_SFX_VOLUME (fRandomVal[1])
        fRandomVal[1] *= 0.8
        SET_AUDIO_STREAM_VOLUME lvar[0] fRandomVal[1]
    ENDIF
RETURN

play_rock_out_sfx_b:
    REMOVE_AUDIO_STREAM lvar[1]
    IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_ro2.mp3"
        LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_ro2.mp3" (lvar[1])
        SET_AUDIO_STREAM_STATE lvar[1] 1    //play
        //SET_AUDIO_STREAM_VOLUME lvar[1] 0.65
        GET_AUDIO_SFX_VOLUME (fRandomVal[1])
        fRandomVal[1] *= 0.65
        SET_AUDIO_STREAM_VOLUME lvar[1] fRandomVal[1]
    ENDIF
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_blur_projector:
    //max_time = 9000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        IF iTempVar = 0
            iTempVar = 1
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
            GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])
            z[0] += 0.1
            CREATE_FX_SYSTEM SP_BPROJECTOR (x[0] y[0] z[0]) 1 (fx_system)
            PLAY_FX_SYSTEM fx_system
            WAIT 0
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
        ENDIF

        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    IF IS_CHAR_ON_SCREEN iChar 
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES iChar x[0] y[0] z[0] 7.0
                            IF NOT IS_CHAR_FALLEN_ON_GROUND iChar
                                IF GOSUB is_not_playing_anim_b
                                    GOSUB assign_new_task_char_blur_reaction
                                    WAIT 0
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE

        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 6.5
            SHAKE_CAM 10
        ENDIF
        IF NOT LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 30.0
            BREAK
        ENDIF
        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE

    REMOVE_AUDIO_STREAM sfx
    KILL_FX_SYSTEM fx_system
    WAIT 50
RETURN

assign_new_task_char_blur_reaction:
    GOSUB assign_char_reference

    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    OPEN_SEQUENCE_TASK anim_seq
        //TASK_LOOK_ABOUT -1 1000
        //TASK_PLAY_ANIM -1 ("EV_step" "ped") 33.0 (0 1 1 0) -1
        TASK_PLAY_ANIM -1 ("IDLE_chat" "ped") 201.0 (0 1 1 0) -1
    CLOSE_SEQUENCE_TASK anim_seq
    PERFORM_SEQUENCE_TASK iChar anim_seq
    WAIT 1
    CLEAR_SEQUENCE_TASK anim_seq
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_low_gravity:
    //0x863984 - [float] Gravity (default value: 1.0f/125.0f = 0.008f)
    CONST_FLOAT default_gravity 0.008   // 1.0/125.0
    CONST_FLOAT moon_gravity 0.00136    // 0.17/125.0
    CONST_FLOAT mercurio_gravity 0.00304    // 0.38/125.0
    CONST_FLOAT venus_gravity 0.0072    // 0.9/125.0

    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 25
    // Set new Gravity
    fRandomVal[0] = venus_gravity
    WRITE_MEMORY 0x0863984 4 fRandomVal[0] FALSE

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        IF NOT IS_BUTTON_PRESSED PAD1 CROSS   // ~k~~PED_SPRINT~    //swing key

            GET_CHAR_VELOCITY player_actor (x[0] y[0] z[0])
            IF -4.0 >= z[0]
                z[0] = -4.0
            ENDIF
            IF IS_CHAR_REALLY_IN_AIR player_actor
                x[1] = x[0]
                y[1] = y[0] * 1.002
                z[1] = z[0]
                SET_CHAR_VELOCITY player_actor x[1] y[1] z[1]
                //PRINT_FORMATTED_NOW "x:%.2f y:%.2f z:%.2f" 1 x[1] y[1] z[1]
            ENDIF

        ENDIF
        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE

    REMOVE_AUDIO_STREAM sfx
    //Restore Gravity
    fRandomVal[0] = default_gravity
    WRITE_MEMORY 0x0863984 4 fRandomVal[0] FALSE
    //PRINT_FORMATTED_NOW "END GRAVITY" 500
    WAIT 50
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_iron_arms:               
    //max_time = 65000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 10
    //CLEAR_CHAR_TASKS player_actor
    //CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
    GOSUB destroyArms_Object
    WAIT 0

    IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        GOSUB createArms_Object
        WAIT 0
        SET_OBJECT_SCALE iChar 1.0
    ELSE
        GOSUB create_iron_arms_render_object
    ENDIF

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        IF NOT IS_CHAR_REALLY_IN_AIR player_actor
            GOSUB play_arms_ground_anim        
        ENDIF

        // damage
        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
        WHILE GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 4.5 1 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
                IF GOSUB is_playing_other_hit_anim  
                    DAMAGE_CHAR iChar 5 TRUE                      
                ENDIF   
            ENDIF      
            WAIT 0 
        ENDWHILE        

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF          
        WAIT 0
    ENDWHILE
    DELETE_RENDER_OBJECT i
/*
    GOSUB destroyArms_Object
    WAIT 0
    IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        GOSUB createArms_Object
        WAIT 0
        SET_OBJECT_SCALE iChar 1.0
        GOSUB play_arms_ground_anim_deactivate
    ENDIF    
*/
    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN    

createArms_Object:
    IF NOT DOES_OBJECT_EXIST iObj
        REQUEST_MODEL 3100      //k_poolballspt02
        REQUEST_MODEL 6028      //iron_arms_hier_object  
        LOAD_ALL_MODELS_NOW

        CREATE_OBJECT 3100 0.0 0.0 0.0 (iObj)
        SET_OBJECT_COLLISION iObj FALSE
        SET_OBJECT_SCALE iObj 0.001
        //hier object
        //iChar = iObj2 
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor 0.0 0.0 0.0 (x[0] y[0] z[0])
        CREATE_OBJECT 6028 (x[0] y[0] z[0]) (iChar)
        SET_OBJECT_SCALE iChar 0.001
        SET_OBJECT_COLLISION iChar FALSE
        SET_OBJECT_PROOFS iChar (1 1 1 1 1)
        ATTACH_OBJECT_TO_OBJECT iObj iChar (0.0 -0.05 0.09) (0.0 0.0 0.0)
        TASK_PICK_UP_OBJECT player_actor iChar (-0.08 0.0 0.0) (1 16) "NULL" "NULL" 1   // iChar = iObj2 , to save variables  
        GOSUB set_rotation     
        //WAIT 1000 
        MARK_MODEL_AS_NO_LONGER_NEEDED 6028
        MARK_MODEL_AS_NO_LONGER_NEEDED 3100
    ENDIF
RETURN

create_iron_arms_render_object:
    i = 0
    REQUEST_MODEL 6025
    LOAD_ALL_MODELS_NOW
    CREATE_RENDER_OBJECT_TO_CHAR_BONE player_actor 6025 4 (0.0 0.0 0.0) (0.0 0.0 0.0) i
    SET_RENDER_OBJECT_ROTATION i (83.0 90.0 90.0)
    //SET_RENDER_OBJECT_POSITION i (-0.2 0.2 0.36)
    SET_RENDER_OBJECT_POSITION i (-0.65 0.2 0.01)
RETURN


destroyArms_Object:
    IF DOES_OBJECT_EXIST iObj
        DELETE_OBJECT iObj
    ENDIF
    IF DOES_OBJECT_EXIST iChar
        DELETE_OBJECT iChar
    ENDIF
RETURN

set_rotation:
    IF DOES_OBJECT_EXIST iObj
    AND DOES_OBJECT_EXIST iChar
        GET_CHAR_HEADING player_actor (fAngle[0])
        SET_OBJECT_ROTATION iObj 0.0 0.0 fAngle[0]
        SET_OBJECT_ROTATION iChar 0.0 0.0 fAngle[0]
    ENDIF
RETURN

play_arms_ground_anim:
    // initialize    
    IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        IF DOES_OBJECT_EXIST iObj
        AND DOES_OBJECT_EXIST iChar
            
            //play animation
            TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor ("iron_armsA" "spider") 26.0 (0 1 1 1) -1
            WAIT 5
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("iron_armsA") 

                GET_CHAR_ANIM_CURRENT_TIME player_actor ("iron_armsA") (fRandomVal[0])
                IF fRandomVal[0] > 0.99
                    BREAK
                ELSE
                    IF DOES_OBJECT_EXIST iChar
                        PLAY_OBJECT_ANIM iChar ("iron_armsB" "spider") /*fdelta*/60.0 /*lockF*/1 /*loop*/0
                        SET_OBJECT_ANIM_CURRENT_TIME iChar "iron_armsB" fRandomVal[0]
                        SET_OBJECT_ANIM_SPEED iChar "iron_armsB" 0.0     
                    ENDIF                                                                                                                                         
                ENDIF    

                IF iTempVar = 1
                    IF IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER1
                    AND IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                        GOSUB destroyArms_Object 
                        //GOSUB create_iron_arms_render_object          
                        BREAK
                    ENDIF                          
                ENDIF

                GOSUB set_rotation 

                WAIT 0
            ENDWHILE                    

            GOSUB create_iron_arms_render_object
            GOSUB destroyArms_Object
            CLEAR_CHAR_TASKS player_actor

        ENDIF
    ENDIF
RETURN

play_arms_ground_anim_deactivate:
    // initialize
    IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        IF DOES_OBJECT_EXIST iObj
            //play animation
            PLAY_OBJECT_ANIM iChar ("iron_armsC" "spider") /*fdelta*/60.0 /*lockF*/1 /*loop*/0
            fRandomVal[0] = 0.0
            timera = 0    
            WHILE 250 > timera 
                SET_OBJECT_ANIM_CURRENT_TIME iChar "iron_armsC" fRandomVal[0]
                SET_OBJECT_ANIM_SPEED iChar "iron_armsC" 0.0
                GOSUB set_rotation
                WAIT 0
                fRandomVal[0] += 0.05
            ENDWHILE
            WAIT 0
            timera = 0
            WHILE 350 > timera   
                GOSUB set_rotation
                IF IS_BUTTON_JUST_PRESSED PAD1 SQUARE
                OR IS_BUTTON_JUST_PRESSED PAD1 CROSS
                    BREAK
                ENDIF                      
                WAIT 0     
            ENDWHILE                    
            GOSUB destroyArms_Object
        ENDIF
    ENDIF
RETURN

//------------------------CHECKS-------------------------------
is_player_playing_other_anims:
    IF IS_CHAR_PLAYING_ANIM player_actor ("dodge_front")
    OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_back")
    OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_right")
    OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_left")
        RETURN_TRUE 
        RETURN
    ELSE
        IF IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_b")
        OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_c")
        OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_right_b")
        OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_left_b")
            RETURN_TRUE
            RETURN
        ELSE
            IF IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_b")
            OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_b_ha")
            OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_right_c")
            OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_left_c")
                RETURN_TRUE
                RETURN
            ELSE
                IF IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_c")
                OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_front_c_ha")
                OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_d")
                OR IS_CHAR_PLAYING_ANIM player_actor ("dodge_back_e")                
                    RETURN_TRUE
                    RETURN
                ELSE
                    IF IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_in")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("webstrike_g_out")
                    OR IS_CHAR_PLAYING_ANIM player_actor ("groundToLampB")  //Angle controlled in sp_ml
                        RETURN_TRUE
                        RETURN    
                    ELSE
                        IF IS_CHAR_PLAYING_ANIM player_actor ("c_idle_Z")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_00")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_01")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("c_right_A_02")
                        OR IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_00")
                            RETURN_TRUE
                            RETURN                  
                        ELSE
                            IF IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_01")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("c_left_A_02")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_00")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("c_right_B_01")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_00")
                            OR IS_CHAR_PLAYING_ANIM player_actor ("c_left_B_01")
                                RETURN_TRUE
                                RETURN 
                            ELSE
                                IF IS_CHAR_PLAYING_ANIM player_actor ("c_hit_front")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("c_hit_fall")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("c_hit_center")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("c_hit_left")
                                OR IS_CHAR_PLAYING_ANIM player_actor ("c_hit_right")
                                    RETURN_TRUE
                                    RETURN
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF        
                ENDIF        
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE                               
RETURN

// Old Code         
/*
        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
        WHILE GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 4.5 1 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar

                IF GOSUB is_playing_other_hit_anim  
                    DAMAGE_CHAR iChar 5 TRUE                      
                ENDIF   

            ENDIF      
            WAIT 0 
        ENDWHILE
*/ 

//------------------------------------------------------------

//------------------------------------------------------------
assign_defence_shield:
    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera
        SET_CHAR_PROOFS player_actor TRUE FALSE TRUE TRUE TRUE   //bullet|fire|explosion|collision|melee

        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF IS_CHAR_ON_SCREEN iChar 
                    IF IS_CHAR_SHOOTING iChar
                        CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_BP player_actor (0.0 0.20 0.15) (90.0 0.0 90.0) 4 (fx_system)
                        PLAY_AND_KILL_FX_SYSTEM fx_system
                        WAIT 0
                        //CREATE_FX_SYSTEM_ON_CHAR_WITH_DIRECTION SP_BP player_actor (0.0 -0.20 0.15) (90.0 0.0 90.0) 4 (fx_system)
                        //PLAY_AND_KILL_FX_SYSTEM fx_system
                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE
    REMOVE_AUDIO_STREAM sfx
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee
    WAIT 50
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_spirit_fire:
    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 0
    SET_CHAR_PROOFS player_actor FALSE TRUE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee

    CREATE_FX_SYSTEM_ON_CHAR SP_SPIRIT_FIRE player_actor (0.0 0.0 -0.85) 1 (fx_system)
    PLAY_FX_SYSTEM fx_system
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
                IF DOES_CHAR_EXIST iChar
                AND NOT IS_CHAR_DEAD iChar
                AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                    IF IS_CHAR_ON_SCREEN iChar 
                        IF LOCATE_CHAR_DISTANCE_TO_CHAR player_actor iChar 2.0
                            DAMAGE_CHAR iChar 1 TRUE                            
                            WAIT 1000
                            GET_CLEO_SHARED_VAR varHitCount counter
                            counter ++
                            SET_CLEO_SHARED_VAR varHitCount counter                              
                        ENDIF
                    ENDIF
                ENDIF
        ENDWHILE

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE
    KILL_FX_SYSTEM fx_system
    REMOVE_ALL_SCRIPT_FIRES

    REMOVE_AUDIO_STREAM sfx
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee
    WAIT 50
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_quips:
    //max_time = 6500    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    GENERATE_RANDOM_INT_IN_RANGE 1 11 (iTempVar)
    GOSUB playQuips
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera


        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE
    KILL_FX_SYSTEM fx_system
    REMOVE_ALL_SCRIPT_FIRES

    REMOVE_AUDIO_STREAM sfx
    SET_CHAR_PROOFS player_actor FALSE FALSE FALSE TRUE FALSE   //bullet|fire|explosion|collision|melee
    WAIT 50
RETURN

playQuips:
    SWITCH iTempVar
        CASE 1  //in
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_1.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_1.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 0.8
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK
        CASE 2
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_2.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_2.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK
        CASE 3
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_3.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_3.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK
        CASE 4
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_4.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_4.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK
        CASE 5
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_5.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_5.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK                 
        CASE 6
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_6.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_6.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK  
        CASE 7
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_7.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_7.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK  
        CASE 8
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_8.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_8.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK  
        CASE 9
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_9.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_9.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK  
        CASE 10
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_10.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_10.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK  
        CASE 11
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_q_11.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_q_11.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK                                                         
    ENDSWITCH
RETURN

//------------------------------------------------------------
assign_equalizer:
    //max_time = 100000    //ms 
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx    
    GET_CHAR_HEALTH player_actor counter
    p = counter  
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
        WHILE GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 4.5 1 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar

                IF GOSUB is_playing_other_hit_anim  
                    CLEAR_CHAR_TASKS iChar
                    IF IS_CHAR_HEALTH_GREATER iChar 1
                        DAMAGE_CHAR iChar 100 TRUE 
                        CLEAR_CHAR_TASKS iChar
                    ENDIF                     
                ENDIF  

            ENDIF    
           
            GET_CHAR_HEALTH player_actor counter
            IF HAS_CHAR_BEEN_DAMAGED_BY_WEAPON player_actor WEAPONTYPE_UNARMED
                IF NOT p = counter
                    SET_CHAR_HEALTH player_actor 0 
                ENDIF                
            ELSE
                p = counter
            ENDIF       
            WAIT 0
        ENDWHILE
 
        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE

    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN

is_playing_other_hit_anim:
    IF IS_CHAR_PLAYING_ANIM iChar ("swing_kick_hit_1")     //swing kick hit
    OR IS_CHAR_PLAYING_ANIM iChar ("ground_to_air_hit") //Air combo anims
    OR IS_CHAR_PLAYING_ANIM iChar ("DILDO_Hit_1")
    OR IS_CHAR_PLAYING_ANIM iChar ("DILDO_Hit_2")
    OR IS_CHAR_PLAYING_ANIM iChar ("DILDO_Hit_3")
        RETURN_TRUE
        RETURN
    ELSE
        IF IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_4")
        OR IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_5")
        OR IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_6")
        OR IS_CHAR_PLAYING_ANIM iChar ("dildo_hit_7")
        OR IS_CHAR_PLAYING_ANIM iChar ("HIT_behind")
        OR IS_CHAR_PLAYING_ANIM iChar ("HIT_front")        
            RETURN_TRUE
            RETURN
        ELSE    
            IF IS_CHAR_PLAYING_ANIM iChar ("HitA_1")
            OR IS_CHAR_PLAYING_ANIM iChar ("HitA_2")
            OR IS_CHAR_PLAYING_ANIM iChar ("HitA_3")
            OR IS_CHAR_PLAYING_ANIM iChar ("HIT_back")
            OR IS_CHAR_PLAYING_ANIM iChar ("HIT_L")
            OR IS_CHAR_PLAYING_ANIM iChar ("HIT_R")        
                RETURN_TRUE
                RETURN
            ELSE
                IF IS_CHAR_PLAYING_ANIM iChar ("HIT_walk")
                OR IS_CHAR_PLAYING_ANIM iChar ("Hit_fightkick")       
                OR IS_CHAR_PLAYING_ANIM iChar ("Hit_fightkick_b") 
                OR IS_CHAR_PLAYING_ANIM iChar ("dodge_front_b_hit")
                OR IS_CHAR_PLAYING_ANIM iChar ("dodge_front_c_hita")           
                OR IS_CHAR_PLAYING_ANIM iChar ("dodge_front_c_hitb")            
                    RETURN_TRUE
                    RETURN
                ENDIF
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN
//------------------------------------------------------------

//------------------------------------------------------------
assign_quad_damage:
    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
        WHILE GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 4.5 1 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND IS_CHAR_ON_SCREEN iChar

                IF GOSUB is_playing_other_hit_anim  
                    CLEAR_CHAR_TASKS iChar
                    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
                    IF IS_CHAR_HEALTH_GREATER iChar 15
                        DAMAGE_CHAR iChar 15 TRUE 
                    ENDIF                     
                ENDIF   

            ENDIF      
            WAIT 0
        ENDWHILE  

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE

    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN
//------------------------------------------------------------
assign_king_of_the_ring:
    //max_time = 10000    //ms
    LOAD_TEXTURE_DICTIONARY spaim
    GOSUB loadTextures
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        // start the code here !
        CLEO_CALL get_ped_near_char 0 player_actor 16.5 (iChar)

        IF NOT IS_CHAR_REALLY_IN_AIR player_actor
        AND DOES_CHAR_EXIST iChar

            // L1 
            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~

                IF NOT IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2    // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~  

                    GOSUB process_push_char
                    WAIT 1000                
                    WHILE IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2        // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                        WAIT 0
                    ENDWHILE                                   
                ENDIF
            ENDIF

        ENDIF        

        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE

    REMOVE_TEXTURE_DICTIONARY 
    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN

loadTextures:
    //Textures
    CONST_INT idLR 19 
    LOAD_SPRITE idLR "clr"
RETURN

process_push_char: 
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_DEAD iChar
    AND IS_CHAR_ON_SCREEN iChar

        SET_CHAR_COLLISION iChar FALSE

        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[1] y[1] z[1])
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[0] y[0] z[0])
        GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fAngle[1])
        SET_CHAR_HEADING player_actor fAngle[1]

        GOSUB REQUEST_Animations
        WAIT 0
        CLEAR_CHAR_TASKS player_actor
        CLEAR_CHAR_TASKS_IMMEDIATELY player_actor
        TASK_PLAY_ANIM_WITH_FLAGS player_actor ("yank_object" "spider") 63.0 (0 1 1 0) -1 0 1
        GOSUB playWebSound
        WAIT 0
        fDistance = 0.0
        //SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.25

        IF IS_CHAR_PLAYING_ANIM player_actor ("yank_object")
            WHILE IS_CHAR_PLAYING_ANIM player_actor ("yank_object")

                GET_CHAR_ANIM_CURRENT_TIME player_actor ("yank_object") (fRandomVal[0])
                IF fRandomVal[0] >= 0.129 // frame 8/62   //0.061  //frame 4
                        
                    CLEO_CALL setCharViewPointToCamera 0 player_actor
                    SET_CHAR_ANIM_SPEED player_actor "yank_object" 1.1
                    GET_CHAR_COORDINATES player_actor (x[0] y[0] z[0])
                    GET_GROUND_Z_FOR_3D_COORD x[0] y[0] z[0] (z[0])                    
                    CLEO_CALL linearInterpolation2 0 (0.129 0.774 fRandomVal[0]) (720.0 0.0) (fAngle[1])    //(360*2+90=810)
                    COS fAngle[1] (x[1])
                    SIN fAngle[1] (y[1])
                    x[1] *= 3.0
                    y[1] *= 3.0
                    x[0] += x[1]
                    y[0] += y[1]
                    z[0] += fDistance
                    fDistance +=@ 0.056
                    SET_CHAR_COORDINATES iChar x[0] y[0] z[0] 
                    TASK_PLAY_ANIM_NON_INTERRUPTABLE iChar ("FALL_fall" "ped") 63.0 (0 1 1 0) -1 

                    CONVERT_3D_TO_SCREEN_2D (x[0] y[0] z[0]) TRUE TRUE (x[1] y[1]) (x[2] y[2])
                    CLEO_CALL getActorBonePos 0 player_actor 25 (x[2] y[2] z[2])    //Right hand
                    CONVERT_3D_TO_SCREEN_2D (x[2] y[2] z[2]) TRUE TRUE (x[0] y[0]) (x[2] y[2])
                    CLEO_CALL drawline 0 x[0] y[0] x[1] y[1] 0.5 (255 255 255 255)
                ENDIF
                IF fRandomVal[0] >= 0.774     //frame 48/62   //0.788     //frame 52
                    BREAK
                ENDIF
                WAIT 0             
            ENDWHILE    
            SET_CHAR_COLLISION iChar TRUE    
            IF CLEO_CALL get_target_char_from_char 0 player_actor 35.0 (iTempVar)   //recicled-var
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iTempVar (0.0 0.0 0.8) (x[1] y[1] z[1])
            ELSE
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 9.0 1.5) (x[1] y[1] z[1])
            ENDIF
            CLEO_CALL setCharVelocityTo 0 iChar (x[1] y[1] z[1]) 60.0       
            counter = 0
        ENDIF   

        IF IS_CHAR_REALLY_IN_AIR iChar
            WAIT 250
            CLEO_CALL setSmokeFX 0 iChar (0.0 0.0 -0.5) 25.0
            GET_CLEO_SHARED_VAR varHitCount iTempVar
            iTempVar ++
            SET_CLEO_SHARED_VAR varHitCount iTempVar            

            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
            GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
            GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fRandomVal[0])
            SET_CHAR_HEADING iChar fRandomVal[0]            
            
            CLEAR_CHAR_TASKS_IMMEDIATELY iChar
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("pow_rock_out_h2" "spider") 52.0 (0 1 1 0) -1        //pow_hit_wblm 121.0
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
            WAIT 0
            CLEAR_SEQUENCE_TASK anim_seq            
        ENDIF

        WHILE counter <= 6
            GOSUB is_char_around_char
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_DEAD iChar
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR player_actor iChar
                IF NOT IS_CHAR_IN_ANY_CAR iChar
                AND NOT IS_CHAR_ON_ANY_BIKE iChar
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE iChar
                    IF IS_CHAR_ON_SCREEN iChar 
                        GOSUB assign_task_hit_king_of_ring
                    ENDIF
                ENDIF
            ENDIF              
            WAIT 0
            counter += 1
        ENDWHILE

        WAIT 0
    ENDIF
RETURN

is_char_around_char:
    IF DOES_CHAR_EXIST iChar
        GET_CHAR_COORDINATES iChar (x[0] y[0] z[0])
        IF GET_RANDOM_CHAR_IN_SPHERE_NO_SAVE_RECURSIVE x[0] y[0] z[0] 5.0 0 1 (iChar) 
            IF DOES_CHAR_EXIST iChar
                RETURN_TRUE
                RETURN
            ENDIF
        ENDIF
    ENDIF
    RETURN_FALSE
RETURN

assign_task_hit_king_of_ring:
    GOSUB assign_char_reference
    
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS player_actor (0.0 0.0 0.0) (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
    GET_ANGLE_FROM_TWO_COORDS (x[1] y[1]) (x[0] y[0]) (fRandomVal[0])
    SET_CHAR_HEADING iChar fRandomVal[0]
    CLEAR_CHAR_TASKS iChar
    CLEAR_CHAR_TASKS_IMMEDIATELY iChar
    IF IS_CHAR_HEALTH_GREATER iChar 90
        DAMAGE_CHAR iChar 15 TRUE
        /*IF IS_CHAR_DEAD iChar
            TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500        //pow_hit_wblm
            WAIT 0
        ELSE*/
            OPEN_SEQUENCE_TASK anim_seq
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("pow_rock_out_h2" "spider") 52.0 (0 1 1 0) -1        //pow_hit_wblm 121.0
                TASK_PLAY_ANIM_NON_INTERRUPTABLE -1 ("getup" "ped") 42.0 (0 1 1 0) -1
            CLOSE_SEQUENCE_TASK anim_seq
            PERFORM_SEQUENCE_TASK iChar anim_seq
            WAIT 0
            CLEAR_SEQUENCE_TASK anim_seq
        //ENDIF
    ELSE
        TASK_DIE_NAMED_ANIM iChar ("pow_rock_out_h2" "spider") 16.0 2500    //pow_hit_wblm
        WAIT 0
    ENDIF
    IF IS_CHAR_PLAYING_ANIM iChar "pow_rock_out_h2"
        SET_CHAR_ANIM_SPEED iChar "pow_rock_out_h2" 1.50
    ENDIF
    WAIT 0
    IF IS_CHAR_DEAD iChar
        IF IS_CHAR_SCRIPT_CONTROLLED iChar
            MARK_CHAR_AS_NO_LONGER_NEEDED iChar
        ENDIF
    ENDIF
RETURN

playWebSound:
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull1.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF
        BREAK
        CASE 1
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull2.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 2
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull3.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
        CASE 3
            IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\webPull4.mp3" (sfx)
                SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
                SET_AUDIO_STREAM_STATE sfx 1 
            ENDIF        
        BREAK
    ENDSWITCH
RETURN
//------------------------------------------------------------


/*
//------------------------------------------------------------
assign_fornew:
    //max_time = 10000    //ms
    timera = 0
    iTempVar = 0
    GOSUB play_sfx_general_sfx
    WAIT 0

    WHILE max_time >= timera
        CLEO_CALL set_current_power_progress 0 max_time timera

        // start the code here !


        // break loop
        GOSUB readVars
        IF toggleSpiderMod = 0  //FALSE
        OR isInMainMenu = 1     //1:true 0:false
            BREAK
        ENDIF
        IF NOT IS_PLAYER_PLAYING player
            BREAK
        ENDIF
        IF IS_CHAR_IN_ANY_CAR player_actor
            BREAK
        ENDIF

        WAIT 0
    ENDWHILE

    REMOVE_AUDIO_STREAM sfx
    WAIT 50
RETURN
*/
//------------------------------------------------------------

//------------------GENERAL-----------------------------------
assign_char_reference:
    IF NOT IS_CHAR_SCRIPT_CONTROLLED iChar
        IF CLEO_CALL is_char_gang_ped 0 iChar
            MARK_CHAR_AS_NEEDED iChar
        ENDIF
    ENDIF
RETURN

play_sfx_general_sfx:
    SET_AUDIO_STREAM_STATE sfx 0    //stop
    REMOVE_AUDIO_STREAM sfx
    SWITCH iTempVar
        CASE 0  //in
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_g_start.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_g_start.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 0.8
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                fRandomVal[1] *= 0.8
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
            BREAK
        CASE 1
            IF DOES_FILE_EXIST "CLEO\SpiderJ16D\sfx\powers_g_fall.mp3"
                LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\powers_g_fall.mp3" (sfx)
                SET_AUDIO_STREAM_STATE sfx 1    //play
                //SET_AUDIO_STREAM_VOLUME sfx 1.0
                GET_AUDIO_SFX_VOLUME (fRandomVal[1])
                SET_AUDIO_STREAM_VOLUME sfx fRandomVal[1]
            ENDIF
        BREAK
    ENDSWITCH
RETURN

REQUEST_Animations:
    IF NOT HAS_ANIMATION_LOADED "spider"
        REQUEST_ANIMATION "spider"
        LOAD_ALL_MODELS_NOW
    ELSE
        RETURN
    ENDIF
    WAIT 0
GOTO REQUEST_Animations

//-+----------checks-+---------------
is_not_player_playing_finisher_anims:
    IF NOT IS_CHAR_PLAYING_ANIM player_actor ("finish_1")
    //AND NOT IS_CHAR_PLAYING_ANIM player_actor ("finish_2")
    //AND NOT IS_CHAR_PLAYING_ANIM player_actor ("finish_3")
    //AND NOT IS_CHAR_PLAYING_ANIM player_actor ("finish_4")
        RETURN_TRUE
        RETURN
    ENDIF
    RETURN_FALSE
RETURN

}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL is_power_ready 0
is_power_ready:
    LVAR_INT power_progress
    GET_CLEO_SHARED_VAR varPowersProgress (power_progress)
    IF power_progress >= 99
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL set_current_power_progress 0 iMaxTime ifRandomVal[0]
set_current_power_progress:
    LVAR_INT iMaxTime ifRandomVal[1]  //in
    LVAR_FLOAT fMax_time fCurrent_time fPercentage fMaxPercent
    LVAR_INT power_progress
    fMaxPercent = 100.0
    CSET_LVAR_FLOAT_TO_LVAR_INT (fMax_time) iMaxTime
    CSET_LVAR_FLOAT_TO_LVAR_INT (fCurrent_time) ifRandomVal[0]
    fPercentage = fCurrent_time
    fPercentage /= fMax_time
    fPercentage *= 100.0
    fMaxPercent -= fPercentage
    power_progress =# fMaxPercent
    SET_CLEO_SHARED_VAR varPowersProgress power_progress
CLEO_RETURN 0
}
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*iChar*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT tempPlayer
    LVAR_FLOAT x y z
    LVAR_INT isSolid isCar isActor isObject isParticle
    LVAR_FLOAT xA yA zA xB yB zB 
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer x y z (xA yA zA)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS tempPlayer 0.0 0.0 0.0 (xB yB zB)
    IF IS_LINE_OF_SIGHT_CLEAR xB yB zB xA yA zA (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL getXangleBetweenPoints 0 /*from*/ 0.0 0.0 0.0 /*and*/ 1.0 0.0 0.0 (/*xAngle*/fSyncAngle)
getXangleBetweenPoints:
    LVAR_FLOAT xA yA zA
    LVAR_FLOAT xB yB zB
    LVAR_FLOAT pointY pointZ
    LVAR_FLOAT xAngle
    GET_DISTANCE_BETWEEN_COORDS_2D xA yA xB yB (pointY)
    pointZ = (zA - zB)
    GET_HEADING_FROM_VECTOR_2D pointY pointZ (xAngle)
    xAngle -= 270.0
CLEO_RETURN 0 xAngle
}
{
//CLEO_CALL is_char_gang_ped 0 iChar
is_char_gang_ped:
    LVAR_INT iChar  //in
    LVAR_INT iPedType
    IF DOES_CHAR_EXIST iChar
        GET_PED_TYPE iChar (iPedType)
        IF iPedType = PEDTYPE_GANG1  //Ballas 1
        OR iPedType = PEDTYPE_GANG2  //CJ Gang
        OR iPedType = PEDTYPE_GANG3  //Los Santos Vagos
        OR iPedType = PEDTYPE_GANG4  // San Fierro Rifa
            RETURN_TRUE
        ELSE
            IF iPedType = PEDTYPE_GANG5  // Da Nang Boys
            OR iPedType = PEDTYPE_GANG6  //Mafia
            OR iPedType = PEDTYPE_GANG7  //Mountain Cloud Triad
            OR iPedType = PEDTYPE_GANG8  //Varrio Los Aztecas
                RETURN_TRUE
            ELSE
                RETURN_FALSE
            ENDIF
        ENDIF
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setObjectPosSimple 0 iChar x y z
setObjectPosSimple:
    LVAR_INT hObj // In
    LVAR_FLOAT x y z // In
    LVAR_INT pObj pMatrix pCoord
    IF DOES_OBJECT_EXIST hObj
        GET_OBJECT_POINTER hObj (pObj)
        pMatrix = pObj + 0x14
        READ_MEMORY pMatrix 4 FALSE (pMatrix)
        pCoord = pMatrix + 0x30
        WRITE_MEMORY pCoord 4 (x) FALSE
        pCoord += 0x4 
        WRITE_MEMORY pCoord 4 (y) FALSE
        pCoord += 0x4
        WRITE_MEMORY pCoord 4 (z) FALSE
    ENDIF
CLEO_RETURN 0 ()
}
{
//CLEO_CALL setSmokeFX 0 /*char*/player_actor /*offset*/(0.0 0.0 0.0) /*speed*/ 25.0
setSmokeFX:
    LVAR_INT char
    LVAR_FLOAT xOffset yOffset zOffset
    LVAR_FLOAT speed
    LVAR_FLOAT x[2] y[2] z[2]
    LVAR_FLOAT angle
    IF DOES_CHAR_EXIST char
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS char (xOffset yOffset zOffset) (x[0] y[0] z[0])
        speed *= 0.05
        speed += 1.5
        angle = 0.0
        WHILE 360.0 > angle
            COS angle (x[1]) 
            SIN angle (y[1])
            x[1] *= speed
            y[1] *= speed
            ADD_SMOKE_PARTICLE (x[0] y[0] z[0]) (x[1] y[1] 0.15) (0.8313 0.8313 0.8313 1.0) (0.15) (0.125)
            angle +=@ 15.0
        ENDWHILE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL linearInterpolation 0 (x0 x1 x) (y0 y1) (y)
linearInterpolation:
LVAR_INT iX0   //Min //in
LVAR_INT iX1   //Max
LVAR_INT iX    //Mid
LVAR_INT iY0   //Min
LVAR_INT iY1   //Max

LVAR_FLOAT x0   //Min 
LVAR_FLOAT x1   //Max
LVAR_FLOAT x    //Mid
LVAR_FLOAT y0   //Min
LVAR_FLOAT y1   //Max
CSET_LVAR_FLOAT_TO_LVAR_INT (x0) iX0
CSET_LVAR_FLOAT_TO_LVAR_INT (x1) iX1
CSET_LVAR_FLOAT_TO_LVAR_INT (x) iX
CSET_LVAR_FLOAT_TO_LVAR_INT (y0) iY0
CSET_LVAR_FLOAT_TO_LVAR_INT (y1) iY1
LVAR_FLOAT result[2] 
LVAR_INT iResult
result[0] = (x1 - x0)
IF result[0] = 0.0
    result[1] = (y0 + y1)
    result[1] /= 2.0
ELSE
    y1 = (y1 - y0)
    x1 = (x1 - x0)
    x = (x - x0)
    result[0] = (y1 / x1)
    result[1] = (result[0] * x)
    result[1] = (result[1] + y0)
ENDIF
iResult =# result[1]
CLEO_RETURN 0 iResult     //y0 + (x - x0) * (y1 - y0)/(x1 - x0) 
}
{
//CLEO_CALL linearInterpolation 0 (x0 x1 x) (y0 y1) (y)
linearInterpolation2:
LVAR_FLOAT x0   //Min 
LVAR_FLOAT x1   //Max
LVAR_FLOAT x    //Mid
LVAR_FLOAT y0   //Min
LVAR_FLOAT y1   //Max
LVAR_FLOAT result[2]
result[0] = (x1 - x0)
IF result[0] = 0.0
    result[1] = (y0 + y1)
    result[1] /= 2.0
ELSE
    y1 = (y1 - y0)
    x1 = (x1 - x0)
    x = (x - x0)
    result[0] = (y1 / x1)
    result[1] = (result[0] * x)
    result[1] = (result[1] + y0)
ENDIF
CLEO_RETURN 0 result[1]     //y0 + (x - x0) * (y1 - y0)/(x1 - x0) 
}
{
//CLEO_CALL get_ped_near_char 0 scplayer 20.0 (iNewObj)
get_ped_near_char:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMaxDistance //in
    LVAR_INT i iChar iNewChar
    LVAR_FLOAT x[2] y[2] z[2] fDistance v1 v2
    IF DOES_CHAR_EXIST scplayer
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i iChar)
            IF DOES_CHAR_EXIST iChar
            AND NOT IS_CHAR_ON_ANY_BIKE iChar
            AND NOT IS_CHAR_IN_ANY_CAR iChar
                GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (0.0 0.0 0.0) (x[1] y[1] z[1])
                GET_ACTIVE_CAMERA_COORDINATES (x[0] y[0] z[0])
                GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                IF fMaxDistance >= fDistance
                    IF IS_CHAR_ON_SCREEN iChar

                        CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
                        GET_DISTANCE_BETWEEN_COORDS_2D (339.0 179.0) (v1 v2) (fDistance)

                        IF 30.0 > fDistance
                            iNewChar = iChar
                            BREAK
                        ENDIF

                    ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        IF DOES_CHAR_EXIST iNewChar
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
CLEO_RETURN 0 iNewChar
}
{
//CLEO_CALL get_object_offset_indicator 0 iChar 
get_char_offset_indicator:
    LVAR_INT iChar    //in
    LVAR_INT idModel
    LVAR_FLOAT x[3] y[3] z[3]
    IF DOES_CHAR_EXIST iChar
    AND NOT IS_CHAR_FALLEN_ON_GROUND iChar 
        GET_CHAR_MODEL iChar (idModel)
        GET_MODEL_DIMENSIONS idModel (x[1] y[1] z[1]) (x[2] y[2] z[2])
        x[1] = (x[1] + 0.5)    //0.45
        z[2] = (z[2] - 0.25)   //0.6            
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS iChar (x[1] 0.0 z[2]) (x[0] y[0] z[0])     //x[1] 0.2 z[2]
        RETURN_TRUE  
    ELSE                    
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0 x[0] y[0] z[0]
}
{
//CLEO_CALL setCharViewPointToCamera 0 player_actor
setCharViewPointToCamera:
    LVAR_INT scplayer   //in
    LVAR_FLOAT xPoint yPoint zPoint xPos yPos zPos newAngle
    GET_ACTIVE_CAMERA_POINT_AT xPoint yPoint zPoint
    GET_ACTIVE_CAMERA_COORDINATES xPos yPos zPos
    xPoint = xPoint - xPos
    yPoint = yPoint - yPos
    GET_HEADING_FROM_VECTOR_2D xPoint yPoint (newAngle)
    SET_CHAR_HEADING scplayer newAngle
CLEO_RETURN 0
}
{
//CLEO_CALL getActorBonePos 0 /*actor*/actor /*bone*/0 /*store_to*/var1 var2 var3 
getActorBonePos:
    LVAR_INT scplayer iBone  //in
    LVAR_FLOAT fx fy fz
    LVAR_INT var5 //var6
    GET_PED_POINTER scplayer (scplayer)
    GET_VAR_POINTER (fx) (var5)
    CALL_METHOD 0x5E4280 /*struct*/scplayer /*params*/3 /*pop*/0 /*bUnk*/1 /*nBone*/iBone /*pPoint*/ var5
    /// 0x5E4280 - RwV3d *__thiscall CPed__getBonePosition(RwV3d *vPosition int iBoneID, bool bIncludeAnim)
    /// https://wiki.multitheftauto.com/wiki/GetPedBonePosition
CLEO_RETURN 0 fx fy fz
}
{
//CLEO_CALL get_target_char_from_char 0 scplayer fMaxDistance (char)
get_target_char_from_char:
    LVAR_INT scplayer   //in
    LVAR_FLOAT fMaxDistance  //in
    LVAR_INT p i char iNewPed 
    LVAR_FLOAT xScreenSize x[2] y[2] z[2] fDistance v1 v2
    LVAR_INT pedType
    IF DOES_CHAR_EXIST scplayer
        xScreenSize = 50.0
        i = 0
        WHILE GET_ANY_CHAR_NO_SAVE_RECURSIVE i (i char)
            IF DOES_CHAR_EXIST char
            AND NOT IS_CHAR_DEAD char
            AND NOT IS_INT_LVAR_EQUAL_TO_INT_LVAR scplayer char
                IF NOT IS_CHAR_IN_ANY_CAR char
                AND NOT IS_CHAR_ON_ANY_BIKE char
                AND NOT IS_CHAR_IN_ANY_POLICE_VEHICLE char
                    //GET_PED_TYPE char (pedType)
                    //IF NOT pedType = PEDTYPE_COP
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS char (0.0 0.0 0.0) (x[1] y[1] z[1])
                        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 0.0 0.0) (x[0] y[0] z[0])
                        GET_DISTANCE_BETWEEN_COORDS_3D (x[0] y[0] z[0]) (x[1] y[1] z[1]) (fDistance) 
                        IF fMaxDistance > fDistance
                            IF IS_CHAR_ON_SCREEN char
                            AND IS_LINE_OF_SIGHT_CLEAR (x[1] y[1] z[1]) (x[0] y[0] z[0]) (1 0 0 1 0)   //(isSolid isCar isActor isObject isParticle)
                                CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (x[0] y[0])
                                GET_DISTANCE_BETWEEN_COORDS_2D (339.0 179.0) (v1 v2) (fDistance)
                                IF xScreenSize >= fDistance
                                    xScreenSize = fDistance
                                    iNewPed = char
                                ENDIF
                            ENDIF
                        ENDIF
                    //ENDIF
                ENDIF
            ENDIF
        ENDWHILE
        IF DOES_CHAR_EXIST iNewPed
            RETURN_TRUE
        ELSE
            RETURN_FALSE
        ENDIF
    ENDIF
CLEO_RETURN 0 iNewPed
}
{
//CLEO_CALL setCharVelocityTo 0 iPlayer (x y z) Amp
setCharVelocityTo:
    LVAR_INT scplayer    //in
    LVAR_FLOAT xIn yIn zIn iAmplitude   //in
    LVAR_FLOAT x[2] y[2] z[2] fDistance
    x[1] = xIn
    y[1] = yIn
    z[1] = zIn
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0]
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) fDistance
    x[1] = (x[1] / fDistance)
    y[1] = (y[1] / fDistance)
    z[1] = (z[1] / fDistance)
    x[1] *= iAmplitude
    y[1] *= iAmplitude
    z[1] *= iAmplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
    WAIT 0
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
CLEO_RETURN 0
}
{
//CLEO_CALL drawline 0 x y x1 y1 fThickness r g b a
drawline:
    LVAR_FLOAT x y x1 y1 fThickness //in
    LVAR_INT r g b a    //in
    LVAR_FLOAT fDistance zAngle
    GET_DISTANCE_BETWEEN_COORDS_2D x y x1 y1 (fDistance)
    x1 -= x
    y1 -= y
    GET_HEADING_FROM_VECTOR_2D x1 y1 (zAngle)
    zAngle += 90.0
    x1 /= 2.0
    y1 /= 2.0
    x += x1
    y += y1
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE_WITH_ROTATION 666 x y fDistance fThickness zAngle r g b a
CLEO_RETURN 0
}


//-+---CONSTANTS--------------------
//GLOBAL_CLEO_SHARED_VARS
//100 slots - range 0 to 99
CONST_INT varStatusSpiderMod    0     //1= Mod activated || 0= Mod Deactivated
CONST_INT varHUD                1     //1= Activated     || 0= Deactivated
CONST_INT varMusic              2     //1= Music On	    || 0= Music Off

CONST_INT varHudRadar           3     //sp_hud - MSpiderJ16Dv7
CONST_INT varHudHealth          4     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudAmmo            5     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudMoney           6     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudTime            7     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudBreath          8     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudArmour          9     //sp_hud    ||1= Activated     || 0= Deactivated
CONST_INT varHudWantedS         10    //sp_hud    ||1= Activated     || 0= Deactivated

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:thug hidouts || 4:street crimes || 5:boss2
CONST_INT varCrimesProgress     12    //for stadistics ||MSpiderJ16Dv7
CONST_INT varPcampProgress      13    //for stadistics ||MSpiderJ16Dv7
CONST_INT varCarChaseProgress   14    //for stadistics ||MSpiderJ16Dv7
CONST_INT varScrewBallProgress  15    //for stadistics ||MSpiderJ16Dv7
CONST_INT varBackpacksProgress  16    //for stadistics ||MSpiderJ16Dv7
CONST_INT varLandmarksProgress  17    //for stadistics ||MSpiderJ16Dv7

CONST_INT varBuildingZip        18    //sp_mlb           ||1= Activated     || 0= Deactivated
CONST_INT varBuildingZipFlag    19    //sp_mlb           ||1= Activated     || 0= Deactivated

CONST_INT varAlternativeSwing   20    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varSwingBuilding      21    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFixGround          22    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varMouseControl       23    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varAimSetup           24    // 0:Manual Aim || 1:Auto Aim //sp_dw
CONST_INT varPlayerCanDrive     25    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varFriendlyN          26    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowVehDoors      27    //MSpiderJ16Dv7    ||1= Activated     || 0= Deactivated
CONST_INT varThrowFix           28    //sp_thob          ||1= Activated     || 0= Deactivated

CONST_INT varLevelChar          30    //sp_lvl    || Level
CONST_INT varStatusLevelChar    31    //If value >0 automatically will add that number to Experience Points (Max Reward +2500)

CONST_INT varIdWebWeapon        32    //sp_mm     || 1-8 weap
CONST_INT varWeapAmmo           33    //sp_wep    ||store current weap ammo
CONST_INT varIdPowers           34    //MSpiderJ16Dv7 - sp_po     ||Id powers 1 - 12
CONST_INT varPowersProgress     35    //sp_po     || current power progress
CONST_INT varHitCount           36    //sp_hit    || hitcounting
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting  
CONST_INT varReservoirInactive  38    //sp_res    || disable reservoirs 
CONST_INT varCrimeAlert         39 

CONST_INT varInMenu             40    //1= On Menu       || 0= Menu Closed
CONST_INT varMapLegendLandMark  43    //Show: 1= enable   || 0= disable
CONST_INT varMapLegendBackPack  44    //Show: 1= enable   || 0= disable

CONST_INT varDrugDealProgress   45    //for stadistics ||MSpiderJ16Dv7
CONST_INT varAssaultProgress    46    //for stadistics ||MSpiderJ16Dv7 
CONST_INT varMuggingProgress    47    //for stadistics ||MSpiderJ16Dv7 (Due to messy global shared vars , I have to add it here)

CONST_INT varAudioActive     	49    // 0:OFF || 1:ON  ||global var to check -spech- audio playing

CONST_INT varSkill1             50    //sp_dw    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2             51    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill2a            52    //sp_ev    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3             53    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3a            54    //sp_ml    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3b            55    //sp_me    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c            56    //sp_main  ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c1           57    //sp_mb    ||1= Activated     || 0= Deactivated
CONST_INT varSkill3c2           58    //sp_mb    ||1= Activated     || 0= Deactivated

//Additional Skills
CONST_INT varSkill1a            59    //sp_dw    ||1= Activated     || 0= Deactivated

CONST_INT varFocusCount         70    //sp_hit    || focus bar
CONST_INT varUseFocus           71    //sp_hit    || focus bar