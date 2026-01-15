// Meyvin Tweaks
// Spider-Man Mod for GTA SA c.2018 - 2026
// Compass HUD - Draw & Mask (CLEO+)
// Still In Development Stage !
// You need CLEO+: https://forum.mixmods.com.br/f141-gta3script-cleo/t5206-como-criar-scripts-com-cleo

SCRIPT_START
{
SCRIPT_NAME sp_comp
WAIT 0
LVAR_INT player_actor toggleSpiderMod isInMainMenu toggleHUD hud_mode is_in_interior
LVAR_INT is_hud_enabled is_opening_door isInInterior
LVAR_INT iTempVar iTempVar2 iTempVar3
LVAR_INT rwCompass // compass texture pointer 
LVAR_INT i j    // mask texture pointers   
LVAR_FLOAT cx cy cz drawPosX

GET_PLAYER_CHAR 0 player_actor
GOSUB loadCompassTexture
is_opening_door = FALSE
is_hud_enabled = TRUE
CLEO_CALL get_screen_aspect_ratio 0 iTempVar    // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
CLEO_CALL storeCurrentAspectRatio 0 iTempVar

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
    IF IS_PLAYER_PLAYING player_actor
        GOSUB readVars
        IF toggleSpiderMod = 1 //TRUE

            IF isInMainMenu = 0     //1:true 0: false
                
                IF toggleHUD = 1  // 0:OFF || 1:ON
                    GOSUB readVars
                    GOSUB hudCheck
                    GOSUB openDoorCheck
                    GOSUB activeInteriorCheck                
                        
                    WHILE toggleHUD = 1    
                        GOSUB readVars
                        GOSUB hudCheck
                        GOSUB openDoorCheck
                        GOSUB activeInteriorCheck

                        IF IS_ON_SCRIPTED_CUTSCENE  // checks if the "widescreen" mode is active
                        OR IS_ON_CUTSCENE 
                        //OR IS_HUD_VISIBLE 
                        OR isInInterior = TRUE
                        OR is_hud_enabled = FALSE
                        //OR is_opening_door = TRUE
                            USE_TEXT_COMMANDS FALSE
                        ELSE
                            USE_TEXT_COMMANDS FALSE
                            GOSUB drawCompass
                        ENDIF      
                
                        /*IF is_in_interior = 1   // Disables IF Player Is In Interior
                            IF flag_player_on_mission = 0   // 0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
                                USE_TEXT_COMMANDS FALSE
                                WHILE NOT is_in_interior = 0     //1:true 0: false
                                    GOSUB activeInteriorCheck
                                WAIT 1500
                                ENDWHILE   
                            ENDIF
                        ENDIF  */    

                        IF isInMainMenu = 1         // Disables IF Is In Menu
                            USE_TEXT_COMMANDS FALSE
                            WHILE isInMainMenu = 1     //1:true 0: false
                                GOTO end_hud_script
                                WAIT 0
                            ENDWHILE       
                        ENDIF  
                        WAIT 0     
                    ENDWHILE
         
                ENDIF
            ELSE
                end_hud_script:                   
                USE_TEXT_COMMANDS FALSE
                //DISPLAY_HUD TRUE
                USE_TEXT_COMMANDS FALSE               
                WAIT 25
                //REMOVE_TEXTURE_DICTIONARY
                //WAIT 0
                //TERMINATE_THIS_CUSTOM_SCRIPT
                GOTO start_check            
            ENDIF
        ENDIF      
    ENDIF
    WAIT 0
GOTO main_loop

readVars:
    GET_CLEO_SHARED_VAR varHUD (toggleHUD)
    GET_CLEO_SHARED_VAR varInMenu (isInMainMenu)
    GET_CLEO_SHARED_VAR varStatusSpiderMod (toggleSpiderMod)
RETURN

activeInteriorCheck:
    GET_AREA_VISIBLE (is_in_interior)
RETURN

hudCheck:
    READ_MEMORY 0xBA6769 4 FALSE (hud_mode)
    IF hud_mode = FALSE
        is_hud_enabled = FALSE
    ELSE
        is_hud_enabled = TRUE
    ENDIF
RETURN

openDoorCheck:
    READ_MEMORY 0x96A7CC 4 FALSE (iTempVar)
    IF iTempVar = 1
    OR iTempVar = 2
        is_opening_door = TRUE
    ELSE
        is_opening_door = FALSE
    ENDIF
RETURN

drawCompass:
    CLEO_CALL get_screen_aspect_ratio 0 (iTempVar)      
    CLEO_CALL storeCurrentAspectRatio 0 (iTempVar)  

    //CLEO_CALL getHudRadar 0 (iTempVar3)
    GET_CLEO_SHARED_VAR varHudRadar (iTempVar3)
    IF iTempVar3 = 1    //1:true 0: false
        CLEO_CALL getCurrentAspectRatio 0 (iTempVar)
        //PRINT_FORMATTED_NOW "Aspect Ratio: %i" 1000 iTempVar

        SWITCH iTempVar // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5:4
            CASE 1  //16:9

                GET_LABEL_POINTER BufferMask128 i
                WRITE_STRUCT_OFFSET i 0  4 0.0  // x top left corner  
                WRITE_STRUCT_OFFSET i 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET i 8  4 530.25  // x top right corner 
                WRITE_STRUCT_OFFSET i 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET i 16 4 0.0  // x bottom left corner 
                WRITE_STRUCT_OFFSET i 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET i 24 4 530.25  // x bottom right corner 
                WRITE_STRUCT_OFFSET i 28 4 365.0  // y bottom middle half 2

                GET_LABEL_POINTER BufferMaskB128 j
                WRITE_STRUCT_OFFSET j 0  4 610.25  // x top left corner  
                WRITE_STRUCT_OFFSET j 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET j 8  4 700.0  // x top right corner 
                WRITE_STRUCT_OFFSET j 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET j 16 4 610.25  // x bottom left corner 
                WRITE_STRUCT_OFFSET j 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET j 24 4 700.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET j 28 4 365.0  // y bottom middle half 2

                GET_ACTIVE_CAMERA_ROTATION cx cy cz

                drawPosX = cz * 1.0
                drawPosX += 448.0 // Center of screen

                DRAW_TEXTURE_PLUS rwCompass DRAW_EVENT_AFTER_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 i 255 255 255 255
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 j 255 255 255 0
            
                //PRINT_FORMATTED_NOW "X %f Y %f Z%f drawX %f" 1000 cx cy cz drawX  
                BREAK
            CASE 2  //4:3
                GET_LABEL_POINTER BufferMask128 i
                WRITE_STRUCT_OFFSET i 0  4 0.0  // x top left corner  
                WRITE_STRUCT_OFFSET i 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET i 8  4 510.5  // x top right corner 
                WRITE_STRUCT_OFFSET i 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET i 16 4 0.0  // x bottom left corner 
                WRITE_STRUCT_OFFSET i 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET i 24 4 510.5  // x bottom right corner 
                WRITE_STRUCT_OFFSET i 28 4 365.0  // y bottom middle half 2

                GET_LABEL_POINTER BufferMaskB128 j
                WRITE_STRUCT_OFFSET j 0  4 617.25  // x top left corner  
                WRITE_STRUCT_OFFSET j 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET j 8  4 700.0  // x top right corner 
                WRITE_STRUCT_OFFSET j 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET j 16 4 617.25  // x bottom left corner 
                WRITE_STRUCT_OFFSET j 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET j 24 4 700.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET j 28 4 365.0  // y bottom middle half 2

                GET_ACTIVE_CAMERA_ROTATION cx cy cz


                drawPosX = cz * 1.34
                drawPosX += 238.0 // Center of screen

                DRAW_TEXTURE_PLUS rwCompass DRAW_EVENT_AFTER_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 i 255 255 255 255
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 j 255 255 255 0
                //PRINT_FORMATTED_NOW "X %f Y %f Z%f drawX %f" 1000 cx cy cz drawX  
                BREAK   

            CASE 3  //16:10
                GET_LABEL_POINTER BufferMask128 i
                WRITE_STRUCT_OFFSET i 0  4 0.0  // x top left corner  
                WRITE_STRUCT_OFFSET i 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET i 8  4 535.5  // x top right corner 
                WRITE_STRUCT_OFFSET i 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET i 16 4 0.0  // x bottom left corner 
                WRITE_STRUCT_OFFSET i 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET i 24 4 535.5  // x bottom right corner 
                WRITE_STRUCT_OFFSET i 28 4 365.0  // y bottom middle half 2

                GET_LABEL_POINTER BufferMaskB128 j
                WRITE_STRUCT_OFFSET j 0  4 625.5  // x top left corner  
                WRITE_STRUCT_OFFSET j 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET j 8  4 700.0  // x top right corner 
                WRITE_STRUCT_OFFSET j 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET j 16 4 625.5  // x bottom left corner 
                WRITE_STRUCT_OFFSET j 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET j 24 4 700.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET j 28 4 365.0  // y bottom middle half 2

                GET_ACTIVE_CAMERA_ROTATION cx cy cz


                drawPosX = cz * 1.111
                drawPosX += 310.0 // Center of screen

                DRAW_TEXTURE_PLUS rwCompass DRAW_EVENT_AFTER_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 i 255 255 255 255
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 j 255 255 255 0
                //PRINT_FORMATTED_NOW "X %f Y %f Z%f drawX %f" 1000 cx cy cz drawX  
                BREAK 

            CASE 4  //5:4
                GET_LABEL_POINTER BufferMask128 i
                WRITE_STRUCT_OFFSET i 0  4 0.0  // x top left corner  
                WRITE_STRUCT_OFFSET i 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET i 8  4 497.0  // x top right corner 
                WRITE_STRUCT_OFFSET i 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET i 16 4 0.0  // x bottom left corner 
                WRITE_STRUCT_OFFSET i 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET i 24 4 497.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET i 28 4 365.0  // y bottom middle half 2

                GET_LABEL_POINTER BufferMaskB128 j
                WRITE_STRUCT_OFFSET j 0  4 613.25  // x top left corner  
                WRITE_STRUCT_OFFSET j 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET j 8  4 700.0  // x top right corner 
                WRITE_STRUCT_OFFSET j 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET j 16 4 613.25  // x bottom left corner 
                WRITE_STRUCT_OFFSET j 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET j 24 4 700.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET j 28 4 365.0  // y bottom middle half 2

                GET_ACTIVE_CAMERA_ROTATION cx cy cz


                drawPosX = cz * 1.421
                drawPosX += 210.0 // Center of screen

                DRAW_TEXTURE_PLUS rwCompass DRAW_EVENT_AFTER_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 i 255 255 255 255
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 j 255 255 255 0
                //PRINT_FORMATTED_NOW "X %f Y %f Z%f drawX %f" 1000 cx cy cz drawX  
                BREAK  

            DEFAULT
                GET_LABEL_POINTER BufferMask128 i
                WRITE_STRUCT_OFFSET i 0  4 0.0  // x top left corner  
                WRITE_STRUCT_OFFSET i 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET i 8  4 530.25  // x top right corner 
                WRITE_STRUCT_OFFSET i 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET i 16 4 0.0  // x bottom left corner 
                WRITE_STRUCT_OFFSET i 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET i 24 4 530.25  // x bottom right corner 
                WRITE_STRUCT_OFFSET i 28 4 365.0  // y bottom middle half 2

                GET_LABEL_POINTER BufferMaskB128 j
                WRITE_STRUCT_OFFSET j 0  4 610.25  // x top left corner  
                WRITE_STRUCT_OFFSET j 4  4 338.0  // y top middle half 1
                WRITE_STRUCT_OFFSET j 8  4 700.0  // x top right corner 
                WRITE_STRUCT_OFFSET j 12 4 338.0  // y top middle half 2
                WRITE_STRUCT_OFFSET j 16 4 610.25  // x bottom left corner 
                WRITE_STRUCT_OFFSET j 20 4 365.0  // y bottom middle half 1
                WRITE_STRUCT_OFFSET j 24 4 700.0  // x bottom right corner 
                WRITE_STRUCT_OFFSET j 28 4 365.0  // y bottom middle half 2

                GET_ACTIVE_CAMERA_ROTATION cx cy cz

                drawPosX = cz * 1.0
                drawPosX += 448.0 // Center of screen

                DRAW_TEXTURE_PLUS rwCompass DRAW_EVENT_AFTER_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 i 255 255 255 255
                DRAW_TEXTURE_PLUS 0 DRAW_EVENT_BEFORE_HUD (drawPosX 352.5) (900.0 25.0) 0.0 0.0 TRUE 4 j 255 255 255 0
            
                //PRINT_FORMATTED_NOW "X %f Y %f Z%f drawX %f" 1000 cx cy cz drawX  
                BREAK            
        ENDSWITCH  
        //PRINT_FORMATTED_NOW "Aspect Ratio: %i" 1000 iTempVar 
    ENDIF
RETURN

loadCompassTexture:
    LOAD_TEXTURE_DICTIONARY sphud
    LOAD_SPRITE dCompass "nesw"
    GET_TEXTURE_FROM_SPRITE dCompass rwCompass
RETURN

}
SCRIPT_END

{
//CLEO_CALL get_screen_aspect_ratio 0 var
get_screen_aspect_ratio:
    LVAR_FLOAT val[3] fResX fResY fAspectRatio
    LVAR_INT id
    CLEO_CALL getCurrentResolution 0 (fResX fResY)
    fAspectRatio = fResX
    fAspectRatio /= fResY
    val[0] = 16.0
    val[1] = 9.0
    val[2] = val[0]
    val[2] /= val[1]    //16:9
    IF fAspectRatio = val[2]    //16:9
        id = 1  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
        CLEO_RETURN 0 id
    ELSE
        val[0] = 4.0
        val[1] = 3.0
        val[2] = val[0]
        val[2] /= val[1]    //4:3
        IF fAspectRatio = val[2]    //4:3
            id = 2  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
            CLEO_RETURN 0 id
        ELSE
            val[0] = 16.0
            val[1] = 10.0
            val[2] = val[0]
            val[2] /= val[1]    //16:10
            IF fAspectRatio = val[2]    //16:10
                id = 3  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                CLEO_RETURN 0 id
            ELSE
                val[0] = 5.0
                val[1] = 4.0
                val[2] = val[0]
                val[2] /= val[1]    //5:4
                IF fAspectRatio = val[2]    //5:4
                    id = 4  // id:1 -16:9 | 2: -4:3 |3: - 16:10 |4: 5/4
                    CLEO_RETURN 0 id
                ENDIF
            ENDIF
        ENDIF
    ENDIF
CLEO_RETURN 0 0
}
{
//CLEO_CALL getCurrentResolution 0 (fX fY)
getCurrentResolution:
    LVAR_INT iresX iresY
    LVAR_FLOAT fresX fresY
    GET_CURRENT_RESOLUTION (iresX iresY)
    fresX =# iresX
    fresY =# iresY
CLEO_RETURN 0 (fresX fresY)
}
{
//CLEO_CALL storeCurrentAspectRatio 0 var
storeCurrentAspectRatio:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getCurrentAspectRatio 0 (var)
getCurrentAspectRatio:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER Screen_AspectRatio (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}
{
//CLEO_CALL storeHudRadar 0 var
storeHudRadar:
    LVAR_INT inVal
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_radar_item pActiveItem
    WRITE_MEMORY pActiveItem 4 inVal FALSE
CLEO_RETURN 0
}
{
//CLEO_CALL getHudRadar 0 (var)
getHudRadar:
    LVAR_INT pActiveItem
    GET_LABEL_POINTER GUI_Memory_hud_radar_item (pActiveItem)
    READ_MEMORY (pActiveItem) 4 FALSE (pActiveItem)  
CLEO_RETURN 0 pActiveItem
}



// Memory Thread

Screen_AspectRatio:
DUMP
00000000    //id:1 - 16:9  | id:2 - 4:3
ENDDUMP

GUI_Memory_hud_radar_item:
DUMP
00 00 00 00
ENDDUMP

BufferMask128:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //32
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //64
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //96
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //128
ENDDUMP

BufferMaskB128:
DUMP
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //32
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //64
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //96
00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 //128
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

CONST_INT varOnmission          11    //0:Off ||1:on mission || 2:car chase || 3:criminal || 4:boss1 || 5:boss2
CONST_INT varCrimesProgress     12    //for stadistics ||MSpiderJ16Dv7
CONST_INT varPcampProgress      13    //for stadistics ||MSpiderJ16Dv7
CONST_INT varCarChaseProgress   14    //for stadistics ||MSpiderJ16Dv7
CONST_INT varScrewBallProgress  15    //for stadistics ||MSpiderJ16Dv7
CONST_INT varBackpacksProgress  16    //for stadistics ||MSpiderJ16Dv7
CONST_INT varLandmarksProgress  17    //for stadistics ||MSpiderJ16Dv7

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
CONST_INT varHitCountFlag       37    //sp_hit    || hitcounting and focus bar
CONST_INT varReservoirInactive  38    //sp_res    || disable reservoirs 
CONST_INT varCrimeAlert         39 

CONST_INT varInMenu             40    //1= On Menu       || 0= Menu Closed
CONST_INT varMapLegendLandMark  43    //Show: 1= enable   || 0= disable
CONST_INT varMapLegendBackPack  44    //Show: 1= enable   || 0= disable

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

//Textures
CONST_INT dCompass 1








/*



                                    y top middle half 1         y top middle half 2
            x top left corner    __________________________||_________________________ x top right corner
                                 |                                                   |
                                 |                                                   |
                                 |                                                   |
                                 |                                                   |
                                 |                                                   |
         x bottom left corner    |_________________________||________________________| x bottom right corner

                                   y bottom middle half 1         y bottom middle half 2
  


    */ 

