# off-lattice
Implementation of an off lattice cellular automaton.

## Compilation
### Simulation

```
mvn package
```

## Execution
### Simulation

```
java -jar off-lattice-1.0-SNAPSHOT-jar-with-dependencies.jar -df generator/1-dynamic-100.ari -n 0.1 -t 250 
```
Parameters:

* **-df, --dynamic_file &lt;arg>**: Path to the file with the dynamic values.
* **-h, --help**: Shows the help.
* **-n, --noise &lt;arg>**: Noise of the environment.
* **-rc, --radius &lt;arg>**: Radius of interaction between particles.
* **-rp, --radius_particle &lt;arg>**: Radius of particles.
* **-s, --speed &lt;arg>**: Speed module of the particles.
* **-t, --time &lt;arg>**: Total time of the simulation.
* **-t, --interval &lt;arg>**: Interval of the simulation.
#### Dynamic values
The program accepts an input file that follows this format:
```
l
x1  y1  theta1
x2  y2  theta2
...
xn  yn  thetan
```
Where `l` is the length of the side of the cell where the particles are generated.

You can use the python script `generator/generate_files.py` to generate 
several of these inputs.