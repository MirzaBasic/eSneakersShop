
using eSneakers.DATA.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Runtime.Remoting.Contexts;
using System.Text;
using System.Threading.Tasks;
using System.Web.Configuration;

namespace eSneakers.WEB.Data
{
    public class MojContext : DbContext
    {
        public MojContext() : base("Name=MyConnectionString")
        {


        }
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Conventions.Remove<OneToManyCascadeDeleteConvention>();

        }



        public DbSet<Korisnik> Korisnici { get; set; }

        public DbSet<Proizvod> Proizvodi { get; set; }
        public DbSet<TipProizvoda> TipProizvoda { get; set; }
        public DbSet<Narudzba> Narudzbe { get; set; }
        public DbSet<StavkeNarudzbe> StavkeNaruzbe { get; set; }
        public DbSet<Komentar> Komentari { get; set; }

        public DbSet<Ocjena> Ocjene { get; set; }
    }
}
