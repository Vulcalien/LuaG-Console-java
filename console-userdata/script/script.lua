settransparent(0xff00ff)

function init()
	print("scr_w: " .. scr_w)
	print("scr_h: " .. scr_h)
	print("map_w: " .. map_w)
	print("map_h: " .. map_h)
end

t = 0
function tick()
	if key(3) then print("d") end

	clear(0x555555)
	pix(5, 5, 0xff0000)
	pix(8, 8, 0x00ff00, 5, 2)

	write("ciaoo", 0xffffff, 100, 100)

	spr(t, 50, 0, 0, 8, 8)

	if t % 60 == 0 then sfx("test") end

	t = t + 1
end
