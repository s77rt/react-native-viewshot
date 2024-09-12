import { TurboModule, TurboModuleRegistry } from "react-native";

export interface Spec extends TurboModule {
	capture(nodeHandle: number): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>("RTNViewshot");
