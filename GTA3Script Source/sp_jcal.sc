// by Meyvin Tweaks
// Jameson Daily Bugle Interviews
// Spider-Man Mod for GTA SA c.2018 - 2023
// Original Shine GUI by Junior_Djjr
// Official Page: https://forum.mixmods.com.br/f16-utilidades/t694-shine-gui-crie-interfaces-personalizadas
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

// Time To Start Jameson Interview
CONST_INT time_start_interview 600000   //ms - 10 minutes    120000

// Jameson Interview Time Length
CONST_INT time_interview_1 43500      //ms - JJJ_PInterview_1
CONST_INT time_interview_2 26500      //ms - JJJ_PInterview_2
CONST_INT time_interview_3 31500      //ms - JJJ_PInterview_3
CONST_INT time_interview_4 55500      //ms - JJJ_PInterview_4
CONST_INT time_interview_5 30500      //ms - JJJ_PInterview_5
CONST_INT time_interview_6 26500      //ms - JJJ_PInterview_6
CONST_INT time_interview_7 43500      //ms - JJJ_PInterview_7
CONST_INT time_interview_8 28000      //ms - JJJ_PInterview_8
CONST_INT time_interview_9 38500      //ms - JJJ_PInterview_9
CONST_INT time_interview_10 25500     //ms - JJJ_PInterview_10

SCRIPT_START
{
SCRIPT_NAME sp_jcal
LVAR_INT player_actor iTempVar2 iTempVar3
LVAR_INT toggleSpiderMod isInMainMenu flag_player_on_mission is_radar_enabled
LVAR_INT counter sfx r g b iTempVar audio_line_is_active
LVAR_FLOAT sx sy px py fVolume

GET_PLAYER_CHAR 0 player_actor

start_check:
GOSUB readVars
IF toggleSpiderMod = 0
    WHILE toggleSpiderMod = 0
        GOSUB readVars    
        IF toggleSpiderMod = 1
            IF isInMainMenu = 0  
                BREAK
            ENDIF
        ENDIF
        WAIT 0
    ENDWHILE
ENDIF
timera = 0

//-+-- Start Internal Threads
    //STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_jinterview_internalThread

main_loop:
    IF IS_PLAYER_PLAYING player_actor
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        // start to code
        GOSUB readVars
        IF toggleSpiderMod = 1  //
        
            GOSUB readVars
            IF isInMainMenu = 0     //1:true 0: false

                GOSUB readVars
                IF flag_player_on_mission = 0

                    IF audio_line_is_active = 0

                        GOSUB readVars
                        IF timera > time_start_interview
                            audio_line_is_active = 1
                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active

                            STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_jinterview_internalThread    // Jameson Public Interview Calls
                            
                            GOSUB load_textures_calls_cellphone
                            WHILE TRUE
                                GOSUB readVars
                                IF audio_line_is_active = 1
                                    IF is_radar_enabled = 1     // 0:OFF || 1:ON
                                        px = 568.0
                                        py = 310.0
                                        GOSUB draw_calls_cellphone
                                        CLEO_CALL generate_anim_bars_speech 0 (533.0 310.0) (16 202 211 150)    //(46 117 156 150)
                                    ELSE
                                        px = 60.0
                                        py = 310.0           
                                        GOSUB draw_calls_cellphone
                                        CLEO_CALL generate_anim_bars_speech 0 (25.0 310.0) (16 202 211 150)    //(46 117 156 150)
                                    ENDIF
                                ENDIF
                                GOSUB readVars
                                IF audio_line_is_active = 0
                                    timera = 0
                                    BREAK
                                ENDIF  
                                GOSUB readVars
                                IF isInMainMenu = 1     //1:true 0: false
                                OR toggleSpiderMod = 0      
                                    audio_line_is_active = 0 
                                    SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active 
                                    USE_TEXT_COMMANDS FALSE
                                    WAIT 0
                                    timera = 0
                                    TERMINATE_THIS_CUSTOM_SCRIPT                   
                                    BREAK                           
                                ENDIF     
                                
                                IF flag_player_on_mission > 0 
                                    audio_line_is_active = 0 
                                    SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active 
                                    USE_TEXT_COMMANDS FALSE
                                    WAIT 0
                                    timera = 0                   
                                    BREAK    
                                ENDIF
                                WAIT 0
                            ENDWHILE    
                        ENDIF  
                    ELSE
                        // this will stop this call when mission failed
                        USE_TEXT_COMMANDS FALSE
                        WAIT 0
                        REMOVE_AUDIO_STREAM sfx
                        timera = 30000                       
                    ENDIF
                    //PRINT_FORMATTED_NOW "Time %i" 1000 timera                                           
                ELSE 
                    USE_TEXT_COMMANDS FALSE
                    WAIT 0
                    REMOVE_AUDIO_STREAM sfx
                    timera = 60000                    
                ENDIF                                          
            ELSE
                USE_TEXT_COMMANDS FALSE
                WAIT 0
                REMOVE_AUDIO_STREAM sfx
                //TERMINATE_THIS_CUSTOM_SCRIPT 
                GOTO start_check
            ENDIF             
        ENDIF   
    ENDIF
    WAIT 0
GOTO main_loop

startJamesonInterview:
    GOSUB load_textures_calls_cellphone
    WHILE TRUE
        GOSUB readVars
        IF audio_line_is_active = 1
            IF is_radar_enabled = 1     // 0:OFF || 1:ON
                px = 568.0
                py = 330.0
                GOSUB draw_calls_cellphone
                CLEO_CALL generate_anim_bars_speech 0 (533.0 330.0) (16 202 211 150)    //(46 117 156 150)
            ELSE
                px = 60.0
                py = 310.0           
                GOSUB draw_calls_cellphone
                CLEO_CALL generate_anim_bars_speech 0 (25.0 310.0) (16 202 211 150)    //(46 117 156 150)
            ENDIF
        ENDIF
        GOSUB readVars
        IF audio_line_is_active = 0
            BREAK
        ENDIF
        WAIT 0
    ENDWHILE      
RETURN  

//-+----------------------------------- JJJ Call
load_textures_calls_cellphone:
    IF DOES_DIRECTORY_EXIST "CLEO\SpiderJ16D"
        LOAD_TEXTURE_DICTIONARY spsams
        LOAD_SPRITE idCallBack "calls_b"
        LOAD_SPRITE idCallBC "call_bc"
        LOAD_SPRITE idCallMJ "call_mj"
        LOAD_SPRITE idCallJJJ "call_jjj"
    ELSE
        PRINT_STRING_NOW "~r~ERROR: 'CLEO\SpiderJ16D' folder not found!" 6000
        timera = 0
        WHILE 5500 > timera
            WAIT 0
        ENDWHILE
        TERMINATE_THIS_CUSTOM_SCRIPT
    ENDIF
RETURN

draw_calls_cellphone:
    sx = 125.0
    sy = 80.0
    USE_TEXT_COMMANDS FALSE
    SET_SPRITES_DRAW_BEFORE_FADE TRUE
    DRAW_SPRITE idCallBack (px py) (sx sy) (255 255 255 170)
    SWITCH iTempVar2
        CASE 0  //JJ Jameson
            USE_TEXT_COMMANDS FALSE
            SET_SPRITES_DRAW_BEFORE_FADE FALSE
            DRAW_SPRITE idCallJJJ (px py) (sx sy) (255 255 255 235)
            BREAK
    ENDSWITCH
RETURN

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varOnmission (flag_player_on_mission)
    GET_CLEO_SHARED_VAR varAudioActive (audio_line_is_active)
    GET_CLEO_SHARED_VAR varHudRadar (is_radar_enabled)    
RETURN

}
SCRIPT_END

//-+--- CALL SCM HELPERS
{
//CLEO_CALL generate_anim_bars_speech 0 (posx posy) (r g b a)
generate_anim_bars_speech:
    LVAR_FLOAT posx posy    //in
    LVAR_INT r g b a   //in
    LVAR_FLOAT fSize1 fSize2 fSize3 fSize4 fSize5
    LVAR_FLOAT fRand1 fRand2 fRand3 fRand4 fRand5
    LVAR_FLOAT fPos1 fPos2 fPos3 fPos4 fPos5
    LVAR_INT counter
    counter = 0
    WHILE 12 >= counter
        fSize1 = 100.0
        GENERATE_RANDOM_FLOAT_IN_RANGE 1.0 fSize1 (fRand1)
        fSize1 -= fRand1
        fSize1 /= 100.0
        fSize1 *= -15.0
        fPos1 = fSize1
        fPos1 /= 2.0
        fPos1 += posy
        USE_TEXT_COMMANDS FALSE
        DRAW_RECT posx fPos1 3.0 fSize1 r g b a
        posx += 3.5
        ++counter
    ENDWHILE
CLEO_RETURN 0
}

{
sp_jinterview_internalThread:  
LVAR_INT player_actor iTempVar2 iTempVar3
LVAR_INT toggleSpiderMod isInMainMenu flag_player_on_mission audio_line_is_active
LVAR_INT iTempVar sfx
LVAR_FLOAT fVolume

GET_PLAYER_CHAR 0 player_actor
timera = 0

WHILE TRUE
    IF IS_PLAYER_PLAYING player_actor
    AND NOT IS_CHAR_IN_ANY_CAR player_actor

        GOSUB readVars
        IF toggleSpiderMod = 1

            IF isInMainMenu = 0    //1:true 0: false 

                IF flag_player_on_mission = 0
                    GOSUB readVars
                    IF audio_line_is_active = 1 

                        GENERATE_RANDOM_INT_IN_RANGE 0 2 iTempVar
                        //iTempVar = 1
                        GOSUB play_sfx_call_initial  

                        GENERATE_RANDOM_INT_IN_RANGE 0 10 iTempVar3
                        WHILE TRUE
                            SWITCH iTempVar3
                                CASE 0
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_1.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_1
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK
                                CASE 1
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_2.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_2
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK
                                CASE 2
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_3.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_3
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK       
                                CASE 3
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_4.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_4
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK                                                                                                     
                                CASE 4
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_5.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_5
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK  
                                CASE 5
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_6.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_6
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK   
                                CASE 6
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_7.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_7
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK 
                                CASE 7
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_8.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_8
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK 
                                CASE 8
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_9.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_9
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK
                                CASE 9
                                    WAIT 1000
                                    LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\JamesonCalls\JJJ_PInterview_10.mp3" (sfx)
                                    SET_AUDIO_STREAM_STATE sfx 1
                                    GET_AUDIO_SFX_VOLUME (fVolume)
                                    fVolume *= 0.8
                                    SET_AUDIO_STREAM_VOLUME sfx fVolume    
                                    timera = 0                                 
                                    WHILE TRUE
                                        IF timera > time_interview_10
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                        
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                         
                                            TERMINATE_THIS_CUSTOM_SCRIPT                                             
                                            BREAK 
                                        ENDIF
                                        GOSUB readVars
                                        IF audio_line_is_active = 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK     
                                        ENDIF                                   
                                        GOSUB readVars
                                        IF toggleSpiderMod = 0 
                                        OR isInMainMenu = 1
                                        OR flag_player_on_mission > 0
                                            audio_line_is_active = 0
                                            SET_CLEO_SHARED_VAR varAudioActive audio_line_is_active                                          
                                            REMOVE_AUDIO_STREAM sfx
                                            iTempVar = 2
                                            GOSUB play_sfx_call_initial                                              
                                            TERMINATE_THIS_CUSTOM_SCRIPT
                                            BREAK
                                        ENDIF
                                        WAIT 0                                                                                                                      
                                    ENDWHILE
                                    BREAK                                                                                                                                                                                                                   
                                DEFAULT
                                    BREAK
                            ENDSWITCH  
                            WAIT 0
                        ENDWHILE      
                    ENDIF                          
                ENDIF           

            ENDIF  
        ENDIF

    ENDIF
    WAIT 0
ENDWHILE  

play_sfx_call_initial:
    SWITCH iTempVar
		CASE 0
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
                fVolume *= 0.8
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF		
			BREAK    
		CASE 1
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call2.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
                fVolume *= 0.8
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF		
			BREAK
        CASE 2
            IF LOAD_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\p_call_end.mp3" (sfx) 
                SET_AUDIO_STREAM_STATE sfx 1
				GET_AUDIO_SFX_VOLUME (fVolume)
                fVolume *= 0.8
				SET_AUDIO_STREAM_VOLUME sfx fVolume
            ENDIF
            BREAK
    ENDSWITCH
RETURN
}

//-+--- Shine GUI
{
//CLEO_CALL GUI_DrawBoxOutline_WithText 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*ouline*/(1.4 1 1 1 1 200 200 200 200) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0
GUI_DrawBoxOutline_WithText:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_FLOAT outlineSize 
LVAR_INT outlineTop outlineRight outlineBottom outlineLeft outlineR outlineG outlineB outlineA textId formatId
LVAR_FLOAT paddingLeft paddingTop

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Outlines
IF outlineLeft = TRUE //outline side left
    f = sizeX / 2.0  
    h = posX - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineTop = TRUE //outline side top
    f = sizeY / 2.0  
    h = posY - f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF  
IF outlineRight = TRUE //outline side right
    f = sizeX / 2.0    
    h = posX + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT h posY outlineSize sizeY (outlineR outlineG outlineB outlineA)
ENDIF    
IF outlineBottom = TRUE //outline side bottom
    f = sizeY / 2.0  
    h = posY + f
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX h sizeX outlineSize (outlineR outlineG outlineB outlineA)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //Menu Item
            formatId = 8 //Menu Item ACTIVE
        ENDIF  
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT posX posY $gxt
ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL GUI_DrawBox_WithNumber 0 /*pos*/(320.0 240.0) /*siz*/(200.0 200.0) /*color*/(0 0 0 180) /*gxtId*/ -1 /*formatId*/ 1 /*left padding*/ 3.0 /*top padding*/ 1.0 /*number*/ 5
GUI_DrawBox_WithNumber:
// In
LVAR_FLOAT posX posY sizeX sizeY
LVAR_INT r g b a 
LVAR_INT textId formatId
LVAR_FLOAT paddingLeft paddingTop
LVAR_INT iNumber

LVAR_INT i
LVAR_FLOAT f h
LVAR_TEXT_LABEL gxt    
// - Create Box
IF a > 0 // Box
    SET_TEXT_DRAW_BEFORE_FADE TRUE
    DRAW_RECT posX posY sizeX sizeY (r g b a)
ENDIF
// - Create Text
IF textId >= 0 // Text
    STRING_FORMAT gxt "J16D%i" textId
    // Do Padding
    IF paddingLeft = 0.0
        SET_TEXT_CENTRE TRUE
    /*ELSE
        f = sizeX / 2.0
        IF paddingLeft > 0.0 // Padding Left/Right
            posX -= f
        ELSE // to left  
            posX += f
        ENDIF*/
    ENDIF
    posX += paddingLeft
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    READ_MEMORY i 4 FALSE (i)
    IF i = textId
        // Text formats IDs adapted to ACTIVE state
        IF formatId = 7 //
            formatId = 8 // ACTIVE
        ENDIF
        IF formatId = 3 //Small Menu
            formatId = 4 //Small Menu ACTIVE
        ENDIF  
    ENDIF
    CLEO_CALL GUI_SetTextFormatByID 0 (formatId)(h)
    posY -= h
    posY += paddingTop
    DISPLAY_TEXT_WITH_NUMBER (posX posY) $gxt iNumber
ENDIF
CLEO_RETURN 0
}
// --- Format IDs
{
GUI_SetTextFormatByID:
LVAR_INT formatId //In
LVAR_INT i
LVAR_FLOAT g
LVAR_FLOAT xSize ySize
SWITCH formatId
    CASE 1
        GOSUB GUI_TextFormat_Title3_LeftMenu
        CLEO_RETURN 0 3.5
        BREAK
    CASE 2
        GOSUB GUI_TextFormat_Title4_Suits  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 3
        GOSUB GUI_TextFormat_SmallMenu  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 4
        GOSUB GUI_TextFormat_SmallMenu_Active  
        CLEO_RETURN 0 5.0
        BREAK
    CASE 5
        GOSUB GUI_TextFormat_Title2_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 6
        GOSUB GUI_TextFormat_Subtitle_Medium_Names  
        CLEO_RETURN 0 3.5
        BREAK
    CASE 7
        GOSUB GUI_TextFormat_Title1_Menu  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 8
        GOSUB GUI_TextFormat_Title1_Menu_Active  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 9
        GOSUB GUI_TextFormat_Text1_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 10
        GOSUB GUI_TextFormat_Number1_Big_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 11
        GOSUB GUI_TextFormat_Text2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 12
        GOSUB GUI_TextFormat_Text3_Medium  
        CLEO_RETURN 0 3.0
        BREAK
    CASE 13
        GOSUB GUI_TextFormat_Text4_Big  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 14
        GOSUB GUI_TextFormat_Number2_Small_Colour  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 15
        GOSUB GUI_TextFormat_Number3_Small  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 16
        GOSUB GUI_TextFormat_Title5_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 17
        GOSUB GUI_TextFormat_Text5_List_Map  
        CLEO_RETURN 0 4.5
        BREAK
    CASE 18
        GOSUB GUI_TextFormat_Interface_Main_Title  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 19
        GOSUB GUI_TextFormat_Interface_Normal_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 20
        GOSUB GUI_TextFormat_Interface_A_Text  
        CLEO_RETURN 0 0.0
        BREAK
    CASE 21
        GOSUB GUI_TextFormat_Interface_Number_Big  
        CLEO_RETURN 0 0.0
        BREAK
ENDSWITCH

GUI_TextFormat_Title3_LeftMenu:    //1   Title 3 - Suit/SuitMods  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200  
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1 
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN    

GUI_TextFormat_Title4_Suits:     //2     Title 4 - Suits Matrix  (BLUE-SHINY)
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.28 1.05 (xSize ySize)    //0.21 0.98
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    /*
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT 2
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 0 0 0 100
    SET_TEXT_SCALE 0.3 1.0
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
    */
RETURN

GUI_TextFormat_SmallMenu: //3
    SET_TEXT_COLOUR 240 240 240 255  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_SmallMenu_Active: //4    
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.27 1.07 (xSize ySize)    //0.2 1.0
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Title2_Menu:  //5   Title 2  (GRAY)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.09 (xSize ySize)    //0.22 1.02
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 200 200 200 255
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Subtitle_Medium_Names:  //6  Text Medium / Names  (LIGHT-BLUE)
    SET_TEXT_FONT FONT_SUBTITLES
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_WRAPX 640.0
    SET_TEXT_COLOUR 50 180 255 200
    SET_TEXT_EDGE 1 (0 0 0 200)
    SET_TEXT_PROPORTIONAL 1  
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN  

GUI_TextFormat_Title1_Menu:   //7  Title 1    (WHITE-GRAY)
    SET_TEXT_COLOUR 240 240 240 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title1_Menu_Active:  //8  Title 1 Active   (LIGHT-BLUE)
    CLEO_CALL GUI_GetPulseAlpha 0 (i)
    SET_TEXT_COLOUR 50 180 255 i
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE 
RETURN

GUI_TextFormat_Text1_Small_Colour:  //9   (BLUE)
    SET_TEXT_COLOUR 0 95 160 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 200)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.86 (xSize ySize)    //0.16 0.8
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number1_Big_Colour:  //10  BIG Numbers for Level (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.74 2.73 (xSize ySize)    //0.55 2.55
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text2_Small_Colour:  //11   LEVEL letters small (BLUE-SHINY)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.19 0.7 (xSize ySize)    //0.14 0.65
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text3_Medium:  //12     Text Descriptions  (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text4_Big:   //13     Text Names   (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 200
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    GET_FIXED_XY_ASPECT_RATIO 0.38 1.45 (xSize ySize)    //0.28 1.35
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN   

GUI_TextFormat_Number2_Small_Colour:  //14   Level Numbers  (LIGHT-BLUE)
    SET_TEXT_COLOUR 6 253 244 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Number3_Small:  //15   Level XP Numbers  (WHITE)
    SET_TEXT_COLOUR 255 255 255 220  
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.21 0.8 (xSize ySize)    //0.16 0.75
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Title5_Map:  //16  Title Map   (DARK-BLUE)
    SET_TEXT_COLOUR 43 57 58 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (43 57 58 100)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Text5_List_Map:  //17  Text List Map (MAGENTA)
    SET_TEXT_COLOUR 17 242 198 220
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 50)
    GET_FIXED_XY_ASPECT_RATIO 0.26 0.99 (xSize ySize)    //0.19 0.92
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Main_Title:  //18  Text BIG (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    GET_FIXED_XY_ASPECT_RATIO 0.29 1.14 (xSize ySize)    //0.22 1.06
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Normal_Text:  //19  Text (WHITE)
    SET_TEXT_COLOUR 255 255 255 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_A_Text:  //20  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.32 1.25 (xSize ySize)    //0.24 1.17
    GET_FIXED_XY_ASPECT_RATIO 0.26 1.04 (xSize ySize)    //0.20 0.97
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN

GUI_TextFormat_Interface_Number_Big:  //21  Text (BLUE)
    SET_TEXT_COLOUR 63 214 241 255
    SET_TEXT_FONT FONT_SUBTITLES
    SET_TEXT_WRAPX 640.0
    SET_TEXT_EDGE 1 (0 0 0 100)
    //GET_FIXED_XY_ASPECT_RATIO 0.4 1.55 (xSize ySize)    //0.30 1.45
    GET_FIXED_XY_ASPECT_RATIO 0.35 1.35 (xSize ySize)    //0.26 1.26
    SET_TEXT_SCALE xSize ySize
    SET_TEXT_PROPORTIONAL 1
    SET_TEXT_DRAW_BEFORE_FADE FALSE
RETURN
}
// --- Functions
{
GUI_GetPulseAlpha:
    LVAR_INT i
    LVAR_FLOAT g
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha i
    READ_MEMORY i 4 FALSE (g)
    i =# g
CLEO_RETURN 0 i
}
{
GUI_SetAtiveGXT:
    LVAR_INT item //In
    LVAR_INT i
    GET_LABEL_POINTER GUI_Memory_ActiveItem i
    WRITE_MEMORY i 4 item FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Update:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    CONST_FLOAT ItemPulseSpeed 2.0
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    READ_MEMORY pAlpha 4 FALSE (fAlpha)
    READ_MEMORY pStep 1 FALSE (iStep)
    IF iStep = 1
        fAlpha -=@ ItemPulseSpeed  
        IF fAlpha < 180.0
            fAlpha = 180.0
            iStep = 2
        ENDIF
    ELSE //Up
        fAlpha +=@ ItemPulseSpeed  
        IF fAlpha > 255.0
            fAlpha = 255.0
            iStep = 1
        ENDIF
    ENDIF
    WRITE_MEMORY pAlpha 4 fAlpha FALSE
    WRITE_MEMORY pStep 1 iStep FALSE
CLEO_RETURN 0
}
{
GUI_Pulse_Reset:
    LVAR_INT pAlpha pStep iStep
    LVAR_FLOAT fAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha pAlpha
    GET_LABEL_POINTER GUI_Memory_ItemMenuActive_PulseAlpha_Step pStep
    WRITE_MEMORY pAlpha 4 255.0 FALSE
    WRITE_MEMORY pStep 1 1 FALSE
CLEO_RETURN 0
}

// Thread Memory
GUI_Memory_ActiveItem:
DUMP
00 00 00 00
ENDDUMP
  
GUI_Memory_ItemMenuActive_PulseAlpha:
DUMP
00 00 00 00
ENDDUMP

GUI_Memory_ItemMenuActive_PulseAlpha_Step:
DUMP
00
ENDDUMP


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

// Textures ID
CONST_INT idCallBack 60
CONST_INT idCallBC 61
CONST_INT idCallMJ 62
CONST_INT idCallJJJ 63