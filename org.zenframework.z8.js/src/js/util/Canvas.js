HTMLCanvasElement.prototype.getContext2d = function(width, height) {
	var devicePixelRatio = Math.max(window.devicePixelRatio, 1);

	this.width = width * devicePixelRatio;
	this.height = height * devicePixelRatio;

	this.style.width = width + 'px';
	this.style.height = height + 'px';

	var context = this.getContext('2d');
	context.scale(devicePixelRatio, devicePixelRatio);
	return context;
};
