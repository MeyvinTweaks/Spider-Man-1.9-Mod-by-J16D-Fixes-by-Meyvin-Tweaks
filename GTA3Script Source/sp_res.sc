// by J16D
// Reservoir Scripts
// Spider-Man Mod for GTA SA c.2018 - 2026
// Fixes By Meyvin Tweaks
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

//-+---CONSTANTS--------------------
//Textures
CONST_INT objCrosshair  61
CONST_INT player 0

SCRIPT_START
{
SCRIPT_NAME sp_res

LVAR_INT player_actor baseObject iWebActor iWebActorR sfx
LVAR_INT flag_photo_mode onmission toggleSpiderMod isInMainMenu
LVAR_FLOAT x[2] y[2] z[2] rwAngle
LVAR_FLOAT v1 v2 sizeX sizeY fCharSpeed currentTime zAngle fFov
LVAR_INT randomVal iTower rwCross

GET_PLAYER_CHAR 0 player_actor
flag_photo_mode = 0     // 0:false||1:true  
onmission = 0

GOSUB loadTextures
GET_TEXTURE_FROM_SPRITE objCrosshair rwCross
WAIT 150

/*
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_tta_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttb_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttc_InternalThread rwCrosshair   // Old method , causes crash upon reloading save game or new game
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_ttd_InternalThread rwCrosshair
WAIT 0
STREAM_CUSTOM_SCRIPT_FROM_LABEL sp_tte_InternalThread rwCrosshair
WAIT 0
*/

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

main_loop:
    IF IS_PLAYER_PLAYING player 
    AND NOT IS_CHAR_IN_ANY_CAR player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE
            IF isInMainMenu = 0 //TRUE
                GOSUB readVars
                IF onmission = 0                        
                    IF IS_CHAR_REALLY_IN_AIR player_actor

                        GOSUB tower_nearby 
                        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                            GOSUB draw_indicator
                            IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                            AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                            AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                            AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                            AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                IF iTower = 1
                                    zAngle = 360.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    zAngle = 360.0
                                    GOSUB animSequence
                                    GOSUB destroyTwoWebs       
                                ENDIF
                                IF iTower = 2
                                    zAngle = 270.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    zAngle = 270.0
                                    GOSUB animSequence
                                    GOSUB destroyTwoWebs 
                                ENDIF
                                IF iTower = 3
                                    zAngle = 360.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    zAngle = 360.0
                                    GOSUB animSequence
                                    GOSUB destroyTwoWebs 
                                ENDIF
                                IF iTower = 4
                                    zAngle = 270.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    zAngle = 270.0
                                    GOSUB animSequence
                                    GOSUB destroyTwoWebs 
                                ENDIF
                                IF iTower = 5
                                    zAngle = 360.0
                                    SET_CHAR_HEADING player_actor zAngle
                                    GOSUB destroyTwoWebs
                                    GOSUB createTwoWebs

                                    zAngle = 360.0
                                    GOSUB animSequence
                                    GOSUB destroyTwoWebs 
                                ENDIF                                                                                                
                                WAIT 500 
                            ENDIF
                        ELSE

                            GOSUB tower_nearby_2    // opposite angle
                            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                                GOSUB draw_indicator
                                IF IS_BUTTON_PRESSED PAD1 LEFTSHOULDER2         // ~k~~PED_CYCLE_WEAPON_LEFT~/ 
                                AND IS_BUTTON_PRESSED PAD1 RIGHTSHOULDER2       // ~k~~PED_CYCLE_WEAPON_RIGHT~/ 
                                AND NOT IS_BUTTON_PRESSED PAD1 CROSS            // ~k~~PED_SPRINT~
                                AND NOT IS_BUTTON_PRESSED PAD1 SQUARE           // ~k~~PED_JUMPING~
                                AND NOT IS_BUTTON_PRESSED PAD1 CIRCLE           // ~k~~PED_FIREWEAPON~   
                                    IF iTower = 1
                                        zAngle = 180.0
                                        SET_CHAR_HEADING player_actor zAngle
                                        GOSUB destroyTwoWebs
                                        GOSUB createTwoWebs

                                        zAngle = 180.0
                                        GOSUB animSequence
                                        GOSUB destroyTwoWebs
                                    ENDIF
                                    IF iTower = 2
                                        zAngle = 90.0
                                        SET_CHAR_HEADING player_actor zAngle
                                        GOSUB destroyTwoWebs
                                        GOSUB createTwoWebs

                                        zAngle = 90.0
                                        GOSUB animSequence
                                        GOSUB destroyTwoWebs
                                    ENDIF
                                    IF iTower = 3
                                        zAngle = 180.0
                                        SET_CHAR_HEADING player_actor zAngle
                                        GOSUB destroyTwoWebs
                                        GOSUB createTwoWebs

                                        zAngle = 180.0
                                        GOSUB animSequence
                                        GOSUB destroyTwoWebs
                                    ENDIF      
                                    IF iTower = 4
                                        zAngle = 90.0
                                        SET_CHAR_HEADING player_actor zAngle
                                        GOSUB destroyTwoWebs
                                        GOSUB createTwoWebs

                                        zAngle = 90.0
                                        GOSUB animSequence
                                        GOSUB destroyTwoWebs
                                    ENDIF      
                                    IF iTower = 5
                                        zAngle = 180.0
                                        SET_CHAR_HEADING player_actor zAngle
                                        GOSUB destroyTwoWebs
                                        GOSUB createTwoWebs

                                        zAngle = 180.0
                                        GOSUB animSequence
                                        GOSUB destroyTwoWebs
                                    ENDIF                                                                                                            
                                WAIT 500
                                ENDIF
                            ENDIF
                        ENDIF
                    ENDIF
                    //PRINT_FORMATTED_NOW "Tower: %i" 2000 iTower
                ENDIF
            ELSE
                GOSUB readVars 
                IF toggleSpiderMod = 0                   
                    USE_TEXT_COMMANDS FALSE
                    //WAIT 0
                    //REMOVE_TEXTURE_DICTIONARY
                    WAIT 0
                    //TERMINATE_THIS_CUSTOM_SCRIPT
                    GOTO start_check 
                ENDIF
            ENDIF           
        /*ELSE
            GOSUB readVars 
            IF toggleSpiderMod = 0                   
                USE_TEXT_COMMANDS FALSE
                //WAIT 0
                //REMOVE_TEXTURE_DICTIONARY
                WAIT 0
                //TERMINATE_THIS_CUSTOM_SCRIPT
                GOTO start_check
            ENDIF
            */        
        ENDIF
    ENDIF
    WAIT 0
GOTO main_loop

readVars:
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varReservoirInactive (onmission)
RETURN

tower_nearby:
    //Tower 1
    x[1] = -2022.26
    y[1] = 13.982
    z[1] = 61.60
    //-- Coords 1
    x[0] = x[1] + 0.08
    y[0] = y[1] - 9.982        
    z[0] = z[1] - 1.0
    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
        iTower = 1
        RETURN_TRUE
        RETURN
    ELSE
        //Tower 2
        x[1] = -2192.0 
        y[1] = 389.789 
        z[1] = 64.624
        //-- Coords 2
        x[0] = x[1] - 9.982
        y[0] = y[1] - 0.08        
        z[0] = z[1] - 0.75
        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
            iTower = 2
            RETURN_TRUE
            RETURN
        ELSE
            //Tower 3
            x[1] = -1873.89
            y[1] = 900.735
            z[1] = 65.2756
            //-- Coords
            x[0] = x[1] - 0.08
            y[0] = y[1] - 9.982        
            z[0] = z[1] - 0.70    
            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                iTower = 3
                RETURN_TRUE
                RETURN    
            ELSE
                //Tower 4
                x[1] = -1812.37 
                y[1] = 1039.22 
                z[1] = 82.0859
                //-- Coords
                x[0] = x[1] - 9.982
                y[0] = y[1] + 0.08    
                z[0] = z[1] - 0.70
                IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                    iTower = 4
                    RETURN_TRUE
                    RETURN    
                ELSE
                    //Tower 5
                    x[1] = -1589.35
                    y[1] = 951.715
                    z[1] = 34.5971
                    //-- Coords
                    x[0] = x[1] + 0.08
                    y[0] = y[1] - 9.982        
                    z[0] = z[1] - 0.75 
                    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                        iTower = 5
                        RETURN_TRUE
                        RETURN    
                    ENDIF
                ENDIF
            ENDIF
        ENDIF                                                                                  
    ENDIF
    RETURN_FALSE
RETURN

tower_nearby_2:
    //Tower 1
    x[1] = -2022.26
    y[1] = 13.982
    z[1] = 61.60
    //-- Coords 1
    x[0] = x[1] + 0.08
    y[0] = y[1] + 9.982        
    z[0] = z[1] - 1.0
    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
        iTower = 1
        RETURN_TRUE
        RETURN
    ELSE
        //Tower 2
        x[1] = -2192.0 
        y[1] = 389.789 
        z[1] = 64.624
        //-- Coords 2
        x[0] = x[1] + 9.982  
        y[0] = y[1] - 0.08      
        z[0] = z[1] - 0.75   
        IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
            iTower = 2
            RETURN_TRUE
            RETURN
        ELSE
            //Tower 3
            x[1] = -1873.89
            y[1] = 900.735
            z[1] = 65.2756
            //-- Coords
            x[0] = x[1] - 0.08
            y[0] = y[1] + 9.982        
            z[0] = z[1] - 0.70 
            IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                iTower = 3
                RETURN_TRUE
                RETURN    
            ELSE
                //Tower 4
                x[1] = -1812.37 
                y[1] = 1039.22 
                z[1] = 82.0859
                //-- Coords
                x[0] = x[1] + 9.982  
                y[0] = y[1] + 0.08      
                z[0] = z[1] - 0.70
                IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                    iTower = 4
                    RETURN_TRUE
                    RETURN    
                ELSE
                    //Tower 5
                    x[1] = -1589.35
                    y[1] = 951.715
                    z[1] = 34.5971
                    //-- Coords
                    x[0] = x[1] + 0.08
                    y[0] = y[1] + 9.982        
                    z[0] = z[1] - 0.75
                    IF LOCATE_CHAR_DISTANCE_TO_COORDINATES player_actor x[0] y[0] z[0] 10.0
                        iTower = 5
                        RETURN_TRUE
                        RETURN    
                    ENDIF
                ENDIF
            ENDIF
        ENDIF                                                                                  
    ENDIF
    RETURN_FALSE
RETURN

animSequence:
    GET_CHAR_SPEED player_actor (fCharSpeed)
    WAIT 0
    SET_CHAR_COLLISION player_actor FALSE
    WAIT 1
    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
    WAIT 0
    SET_CHAR_COLLISION player_actor TRUE
    WAIT 0
    IF flag_photo_mode = 0
        fCharSpeed += 10.0
        CLAMP_FLOAT fCharSpeed 6.0 15.0 (fCharSpeed)
    ELSE        //Default
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF
    IF fCharSpeed < 13.0
        GET_CHAR_SPEED player_actor (fCharSpeed)
        fCharSpeed += 13.0
        CLAMP_FLOAT fCharSpeed 7.0 13.0 (fCharSpeed)
    ENDIF      
    WAIT 0   
    GOSUB playWebSound
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player_actor "t_tower_A" "spider" 91.0 (0 1 1 0) -2
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("w_tower_L_A" "mweb") 91.0 (0 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("w_tower_R_A" "mweb") 91.0 (0 1 1 1) -2
    ENDIF
    WAIT 0
    SET_CHAR_ANIM_SPEED player_actor "t_tower_A" 1.6
    IF DOES_CHAR_EXIST iWebActor
    AND DOES_CHAR_EXIST iWebActorR
        SET_CHAR_ANIM_PLAYING_FLAG iWebActor ("w_tower_L_A") 0
        SET_CHAR_ANIM_PLAYING_FLAG iWebActorR ("w_tower_R_A") 0
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        ATTACH_OBJECT_TO_CHAR baseObject player_actor (0.0 0.0 0.0) (0.0 0.0 0.0)
    ENDIF
    IF IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
        WHILE IS_CHAR_PLAYING_ANIM player_actor ("t_tower_A")
            SET_CHAR_HEADING player_actor zAngle
            GET_CHAR_ANIM_CURRENT_TIME player_actor ("t_tower_A") (currentTime)
            IF DOES_CHAR_EXIST iWebActor
            AND DOES_CHAR_EXIST iWebActorR
                SET_CHAR_ANIM_CURRENT_TIME iWebActor ("w_tower_L_A") currentTime
                SET_CHAR_ANIM_CURRENT_TIME iWebActorR ("w_tower_R_A") currentTime
            ENDIF
            CLEO_CALL setCameraOnChar 0 (x[1] y[1] z[1]) player_actor flag_photo_mode    // 1:normal||0:Photo_mode

            IF 0.200 >= currentTime     //frame 18/90
                IF flag_photo_mode = 0    //Smooth
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0]
                    CLEO_CALL addForceToChar 0 player_actor 0.0 0.01 0.0 3.5
                ELSE                //Default  (for photo mode)         
                    SET_CHAR_COORDINATES_SIMPLE player_actor x[0] y[0] z[0] 
                ENDIF
            ELSE
                IF IS_OBJECT_ATTACHED baseObject
                    //GET_CAMERA_FOV fFov
                    //CAMERA_SET_LERP_FOV 105.0 fFov 5 FALSE
                    DETACH_OBJECT baseObject (0.0 0.0 0.0) FALSE
                    SET_OBJECT_COORDINATES baseObject (x[0] y[0] z[0])
                    GOSUB playSFXSound
                ENDIF
                GOSUB add_force_to_char
                IF  currentTime > 0.778     //frame 70/90
                    BREAK
                ENDIF
            ENDIF
            WAIT 0
        ENDWHILE
    ENDIF
    RESTORE_CAMERA
    RESTORE_CAMERA_JUMPCUT
RETURN

add_force_to_char:
    IF  0.722 > currentTime     //frame 65/90
        CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        fCharSpeed -=@ 0.1 //0.01
        IF 4.0 > fCharSpeed
            fCharSpeed = 4.0
        ENDIF              
    ELSE
        IF  0.778 >= currentTime     //frame 70/90
            fCharSpeed +=@ 0.1
            CLEO_CALL addForceToChar 0 player_actor 0.0 1.0 0.0 fCharSpeed
        ENDIF
    ENDIF
RETURN

draw_indicator:
    IF flag_photo_mode = 0  // 0:false||1:true
        IF IS_POINT_ON_SCREEN (x[1] y[1] z[1]) 0.5
            CONVERT_3D_TO_SCREEN_2D (x[1] y[1] z[1]) TRUE TRUE (v1 v2) (sizeX sizeY)
            GET_FIXED_XY_ASPECT_RATIO 40.0 40.0 (sizeX sizeY)
            //DRAW_SPRITE objCrosshair (v1 v2) (sizeX sizeY) (255 255 255 200)
            rwAngle += 3.0
            IF rwAngle >= 360.0
                rwAngle = 0.0
            ENDIF
            DRAW_TEXTURE_PLUS rwCross DRAW_EVENT_BEFORE_HUD (v1 v2) (sizeX sizeY) rwAngle 0.0 FALSE -1 -1 (255 255 255 200)            
            USE_TEXT_COMMANDS FALSE
        ENDIF
    ENDIF
RETURN

createTwoWebs:
    IF NOT DOES_CHAR_EXIST iWebActor
    AND NOT DOES_CHAR_EXIST iWebActorR
    AND NOT DOES_OBJECT_EXIST baseObject
        REQUEST_MODEL 1598
        LOAD_SPECIAL_CHARACTER 9 wmt
        LOAD_ALL_MODELS_NOW

        //CREATE_OBJECT 1598 0.0 0.0 0.0 (baseObject)
        CREATE_OBJECT_NO_SAVE 1598 0.0 0.0 0.0 FALSE FALSE (baseObject)
        SET_OBJECT_COLLISION baseObject FALSE
        SET_OBJECT_RECORDS_COLLISIONS baseObject FALSE
        SET_OBJECT_SCALE baseObject 0.01
        SET_OBJECT_PROOFS baseObject (1 1 1 1 1)
        MARK_MODEL_AS_NO_LONGER_NEEDED 1598

        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActor
        SET_CHAR_COLLISION iWebActor 0
        SET_CHAR_NEVER_TARGETTED iWebActor 1
        CREATE_CHAR PEDTYPE_CIVMALE SPECIAL09 (0.0 0.0 -10.0) iWebActorR
        SET_CHAR_COLLISION iWebActorR 0
        SET_CHAR_NEVER_TARGETTED iWebActorR 1
        UNLOAD_SPECIAL_CHARACTER 9

        ATTACH_CHAR_TO_OBJECT iWebActor baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        ATTACH_CHAR_TO_OBJECT iWebActorR baseObject (0.0 0.0 0.0) 0 0.0 WEAPONTYPE_UNARMED
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActor ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2
        TASK_PLAY_ANIM_NON_INTERRUPTABLE iWebActorR ("m_idleWeb" "mweb") 5.0 (1 1 1 1) -2

        GET_CHAR_HEADING player_actor (zAngle)
        SET_OBJECT_HEADING baseObject zAngle        
    ENDIF
RETURN

destroyTwoWebs:
    IF DOES_CHAR_EXIST iWebActor
        DELETE_CHAR iWebActor
    ENDIF
    IF DOES_CHAR_EXIST iWebActorR
        DELETE_CHAR iWebActorR
    ENDIF
    IF DOES_OBJECT_EXIST baseObject
        DELETE_OBJECT baseObject
    ENDIF
RETURN

playSFXSound:
    REMOVE_AUDIO_STREAM sfx
    IF LOAD_3D_AUDIO_STREAM "CLEO\SpiderJ16D\sfx\web1_f.mp3" (sfx)
        SET_PLAY_3D_AUDIO_STREAM_AT_CHAR sfx player_actor
        SET_AUDIO_STREAM_STATE sfx 1 
        SET_AUDIO_STREAM_VOLUME sfx 0.5
    ENDIF
RETURN

playWebSound:
    REMOVE_AUDIO_STREAM sfx
    GENERATE_RANDOM_INT_IN_RANGE 0 4 (randomVal)
    SWITCH randomVal
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

loadTextures:
    IF NOT HAS_ANIMATION_LOADED "spider"
    OR NOT HAS_ANIMATION_LOADED "mweb"
        REQUEST_ANIMATION "spider"
        REQUEST_ANIMATION "mweb"
        LOAD_TEXTURE_DICTIONARY scrb
        LOAD_SPRITE objCrosshair "ilock"
        LOAD_ALL_MODELS_NOW        
    ENDIF
    // loads when restarting the game
    LOAD_SPRITE objCrosshair "ilock"    
RETURN

}
SCRIPT_END

//-+---------------------------------CALLSCM HELPERS---------------------------------------
{
//CLEO_CALL isClearInSight 0 player_actor (0.0 0.0 -2.0) (/*solid*/ 1 /*car*/ 1 /*actor*/ 0 /*obj*/ 1 /*particle*/ 0)
isClearInSight:
    LVAR_INT scplayer   //in
    LVAR_FLOAT x y z    //in
    LVAR_INT isSolid isCar isActor isObject isParticle  //in
    LVAR_FLOAT xA yA zA xB yB zB
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer x y z (xA yA zA)
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (xB yB zB)
    IF IS_LINE_OF_SIGHT_CLEAR xB yB zB xA yA zA (isSolid isCar isActor isObject isParticle)
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL setCameraOnChar 0 /*coords*/(0.0 0.0 0.0) /*char*/player_actor /*mode*/1    // 1:normal||0:Photo_mode
setCameraOnChar:
    LVAR_FLOAT xIn yIn zIn  //in
    LVAR_INT scplayer // In
    LVAR_INT randomVal // In
    LVAR_FLOAT x[2] y[2] z[2]
    IF randomVal = 0    
        // Default
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 -2.5 0.75) (x[0] y[0] z[0])  //0.0 -2.5 0.75
        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
        GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer (0.0 2.5 0.25) (x[1] y[1] z[1])
        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
    ELSE
        //PHOTO MODE
        x[0] = xIn + 10.0 //10.0
        y[0] = yIn
        z[0] = zIn
        SET_FIXED_CAMERA_POSITION (x[0] y[0] z[0]) (0.0 0.0 0.0)
        x[1] = xIn
        y[1] = yIn
        z[1] = zIn
        POINT_CAMERA_AT_POINT x[1] y[1] z[1] JUMP_CUT
    ENDIF
CLEO_RETURN 0
}
{
//CLEO_CALL addForceToChar 0 player_actor /*xVel*/0.0 /*yVel*/1.0 /*zVel*/1.0 /*amp*/20.0
addForceToChar:
    LVAR_INT scplayer    //in
    LVAR_FLOAT addXvel addYvel addZvel amplitude    //in
    LVAR_FLOAT distance
    LVAR_FLOAT x[2] y[2] z[2]
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer 0.0 0.0 0.0 (x[0] y[0] z[0])
    GET_OFFSET_FROM_CHAR_IN_WORLD_COORDS scplayer addXvel addYvel addZvel (x[1] y[1] z[1])
    x[1] -= x[0]
    y[1] -= y[0]
    z[1] -= z[0] 
    GET_DISTANCE_BETWEEN_COORDS_3D (0.0 0.0 0.0) (x[1] y[1] z[1]) (distance)
    x[1] /= distance
    y[1] /= distance
    z[1] /= distance
    x[1] *= amplitude
    y[1] *= amplitude
    z[1] *= amplitude
    SET_CHAR_VELOCITY scplayer x[1] y[1] z[1]
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

//-+-----------------------------------------------------------------------------------------
/*
tw_a1
-2022.26 13.982 61.60
359.9484
tw_a2
-2192.0 389.789 64.624
90.9726
tw_a3
-1873.89 900.735 65.2756
359.9484
tw_a4
-1812.37 1039.22 82.0859
270.9562
tw_a5
-1589.35 951.715 34.5971
179.9319
*/