function key(id)
	return _jinterface:key(id)
end

function settransparent(col)
	_jinterface:settransparent(col)
end

function clear(col)
	_jinterface:clear(col)
end

function pix(x, y, col, w, h)
	if w ~= nil and h ~= nil then
		_jinterface:fill(x, y, x + w - 1, y + h - 1, col)
	else
		_jinterface:pix(x, y, col)
	end
end

function write(txt, col, x, y)
	_jinterface:write(txt, col, x, y)
end

function spr(x, y, xs, ys, ws, hs)
	_jinterface:spr(x, y, xs, ys, ws, hs)
end

function sfx(sound)
	_jinterface:sfx(sound)
end

function gettile(x, y)
	return _jinterface:gettile(x, y)
end

function settile(x, y, id)
	_jinterface:settile(x, y, id)
end
