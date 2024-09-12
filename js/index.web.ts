export default {
	capture(_nodeHandle: number): Promise<string> {
		return Promise.reject(new Error("capture not supported on web"));
	},
};
