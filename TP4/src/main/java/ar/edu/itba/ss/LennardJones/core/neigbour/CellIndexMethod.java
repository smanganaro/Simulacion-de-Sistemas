package ar.edu.itba.ss.LennardJones.core.neigbour;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import javafx.geometry.Point2D;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {

  private final static int[][] DIRECTIONS = new int[][]{
      {0, 0},     // CURRENT
      {-1, 0},    // UP
      {-1, +1},   // UP-RIGHT
      {0, +1},    // RIGHT
      {+1, +1}    // DOWN-RIGHT
  };
  private final static int DIRECTIONS_ROW = 0;
  private final static int DIRECTIONS_COL = 1;

  private final double l;
  private final boolean periodic;
  private int m;
  private double cellLength;

  public CellIndexMethod(final double l, final boolean periodic) {
    this.l = l;
    this.periodic = periodic;
  }

  public Map<Particle, Set<Neighbour>> apply(final List<Particle> particles, final double maxRadius,
                                             final double rc) {

    if (maxRadius < 0) {
      throw new IllegalArgumentException("Invalid max radius");
    }

    this.m = (int) Math.floor(l / (rc + 2 * maxRadius));
    this.cellLength = l / m;

    if (rc > cellLength) {
      throw new IllegalArgumentException(
          "Cutoff distance has to be less than cell length (" + cellLength + ").");
    }

    final Map<Particle, Set<Neighbour>> neighboursParticles = new HashMap<>();
    for (final Particle particle : particles) {
      neighboursParticles.put(particle, new HashSet<>());
    }

    final List<Particle>[][] matrix = createMatrix(particles);
    for (int row = 0; row < m; row++) {
      for (int col = 0; col < m; col++) {
        if (matrix[row][col] != null) {
          addNeighbours(neighboursParticles, matrix, row, col, rc);
        }
      }
    }

    return neighboursParticles;
  }

  public Map<Particle, Set<Neighbour>> apply(final List<Particle> particles, final double rc) {
    if(particles.isEmpty())
        throw new IllegalArgumentException();
    final double maxRadius = getMaxRadius(particles);
    return apply(particles, maxRadius, rc);
  }

  private double getMaxRadius(List<Particle> particles){
    double max = 0;
    for(Particle p : particles){
      if(p.getRadius() > max)
        max = p.getRadius();
    }
    return max;
  }

  private List<Particle>[][] createMatrix(final List<Particle> particles) {
    final List<Particle>[][] matrix = new List[m][m];

    for (final Particle particle : particles) {
      final Point2D position = particle.getPosition();
      final int row = (int) (position.getY() / cellLength);
      final int col = (int) (position.getX() / cellLength);

      if (matrix[row][col] == null) {
        matrix[row][col] = new LinkedList<>();
      }

      matrix[row][col].add(particle);
    }

    return matrix;
  }

  private void addNeighbours(final Map<Particle, Set<Neighbour>> neighbours,
      final List<Particle>[][] matrix, final int currentRow, final int currentCol,
      final double rc) {

    final List<Particle> currentCell = matrix[currentRow][currentCol];

    for (final int[] direction : DIRECTIONS) {
      final int neighbourRow = currentRow + direction[DIRECTIONS_ROW];
      final int neighbourCol = currentCol + direction[DIRECTIONS_COL];

      // Skip if not periodic and row/col are out of bounds
      if (periodic || (neighbourRow >= 0 && neighbourRow < m && neighbourCol >= 0
          && neighbourCol < m)) {

        final List<Particle> neighbourCell = matrix[Math.floorMod(neighbourRow, m)][Math
            .floorMod(neighbourCol, m)];

        if (neighbourCell != null) {
          addNeighboursFromCell(neighbours, currentCell, neighbourCell, neighbourRow, neighbourCol,
              rc);
        }
      }
    }
  }

  private void addNeighboursFromCell(final Map<Particle, Set<Neighbour>> neighbours,
      final List<Particle> currentCell, final List<Particle> neighbourCell, final int neighbourRow,
      final int neighbourCol, final double rc) {

    for (final Particle particle1 : currentCell) {
      for (final Particle particle2 : neighbourCell) {
        if (!particle1.equals(particle2)) {
          final Point2D point1 = particle1.getPosition();
          // Remember: col is x and row is y
          final Point2D point2 = particle2.getPosition()
              .add(coordinateCorrection(neighbourCol), coordinateCorrection(neighbourRow));
          final double distance = point1.distance(point2) - particle1.getRadius() - particle2.getRadius();

          if (distance <= rc) {
            neighbours.get(particle1).add(new Neighbour(particle2, distance));
            neighbours.get(particle2).add(new Neighbour(particle1, distance));
          }
        }
      }
    }
  }

  private double coordinateCorrection(final int cellIndex) {
    if (cellIndex < 0) {
      return -l;
    }

    if (cellIndex >= m) {
      return l;
    }

    return 0;
  }
}
