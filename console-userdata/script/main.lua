function init()
	loadscript("init.lua")

	print("scr_w: " .. scr_w)
	print("scr_h: " .. scr_h)
	print("map_w: " .. map_w)
	print("map_h: " .. map_h)
end

t = 0
x = math.floor(scr_w / 2) - 4
y = math.floor(scr_h / 2) - 4
function tick()
	if key(0) then y = y - 1 end
	if key(1) then x = x - 1 end
	if key(2) then y = y + 1 end
	if key(3) then x = x + 1 end

	clear(0x555555)
	maprender()

	pix(5, 5, 0xff0000)
	pix(8, 8, 0x00ff00, 5, 2)

	write("ciaoo", 0xffffff, 100, 100)

	spr(255, x - 4, y - 4, 1)

	if t % 60 == 0 then sfx("test") end

	t = t + 1
end