# Deathban

Custom plugin for [Zerosmp](https://discord.gg/D8EyC4dUYv) that adds a deathban to the server
for [PaperMC](https://papermc.io/) 1.19 written in Kotlin.

## How it works

When a player dies, they lose lives. When they run out of lives, they are banned from the server for a set amount of
time.

- When a player loses lives, they come back after a set period
- Players lose more lives when killed by another player

## Dependencies

- [AdvancedBan](https://www.spigotmc.org/resources/advancedban.8695/) - For banning players
- [LuckPerms](https://luckperms.net/) - For loading cached player uuids

## Usage

- `/lives [player]` - Shows a player, or another player, how many lives they have left
- `/deaths [page]` - Shows the amount of times a player has lost lives
- `/editlives <player> <amount>` - Edits the amount of lives a player has (requires `deathban.editlives` permission)
- `/showlives` - Makes your lives visible to other players
- `/hidelives` - Hides your lives from other players

## Permissions

- `deathban.editlives` - Allows a player to edit the lives of other players
- `deathban.bypass` - Allows a player to bypass the deathban
- `deathban.seethrough` - Allows a player to see the lives of other players

## Default Config

```yaml
# The limit of lives a player can have. Player lives reset/default to this
livesLimit: 4

# The amount of time until a player's lives are reset
updateInterval: 24

# The amount of time a player will be banned for once they run out of lives
banLength: 24

# The reason passed into AdvancedBan when a player is banned
banReason: "@deathban"

# How many lives a player will lose when they die by a player
playerDeathCost: 2

# How many lives a player will lose when they die by a non-player
nonPlayerDeathCost: 1
```

### Feel free to use this plugin on your server but give credit to me
