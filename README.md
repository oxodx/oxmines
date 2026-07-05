# OxMines

A simple plugin for box-mining/prison servers.

## Features:

- Its easy to use.
- Select areas to create mines using world edit or our custom wand.
- Customize the percentages of ores in each mine.
- Add any block you want to the mines.
- Broadcast when a mine resets.
- Teleport players out of mine to prevent suffocation.

## Commands & Permissions:

All commands require `oxmines.any` to be usable at all. Each subcommand also requires its specific permission below.

| Command                                    | Permission        | Description                                                |
| ------------------------------------------ | ----------------- | ---------------------------------------------------------- |
| `/oxmines add <mine>`                      | `oxmines.add`     | Add a new mine from your WorldEdit or wand selection       |
| `/oxmines clear <mine>`                    | `oxmines.clear`   | Set every block in the mine to air                         |
| `/oxmines delwarp <mine>`                  | `oxmines.delwarp` | Remove the mine's warp point                               |
| `/oxmines info <mine>`                     | `oxmines.info`    | View mine details (size, blocks, regen settings)           |
| `/oxmines list`                            | `oxmines.list`    | List all configured mines                                  |
| `/oxmines pos1`                            | `oxmines.pos1`    | Set position 1 for manual region selection                 |
| `/oxmines pos2`                            | `oxmines.pos2`    | Set position 2 for manual region selection                 |
| `/oxmines reload`                          | `oxmines.reload`  | Reload config, messages, and scheduled tasks               |
| `/oxmines remove <mine>`                   | `oxmines.remove`  | Delete a mine entirely                                     |
| `/oxmines reset <mine>`                    | `oxmines.reset`   | Immediately regenerate the mine's blocks                   |
| `/oxmines rule <mine> set <rule> <value>`  | `oxmines.rule`    | Set a mine rule (regenTime, announceRegen, resetWhenEmpty) |
| `/oxmines set <mine> <block> <percentage>` | `oxmines.set`     | Add or update a block in the mine's composition            |
| `/oxmines setwarp <mine>`                  | `oxmines.setwarp` | Set the mine's teleport warp point                         |
| `/oxmines tp <mine>`                       | `oxmines.tp`      | Teleport to a mine                                         |
| `/oxmines unset <mine> <block>`            | `oxmines.unset`   | Remove a block from the mine's composition                 |
| `/oxmines wand`                            | `oxmines.wand`    | Get the Mine Wand tool for selecting regions               |
